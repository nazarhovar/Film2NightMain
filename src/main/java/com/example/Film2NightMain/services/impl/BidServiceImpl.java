package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.BidDeleteFilmDto;
import com.example.Film2NightMain.dto.BidDto;
import com.example.Film2NightMain.dto.BidInfoDto;
import com.example.Film2NightMain.dto.UserDto;
import com.example.Film2NightMain.entities.*;
import com.example.Film2NightMain.repositories.BidRepository;
import com.example.Film2NightMain.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BidServiceImpl.class);
    private final BidRepository bidRepository;
    private final SessionService sessionService;
    private final UserService userService;
    private final FilmService filmService;
    private final BlockService blockService;

    public BidServiceImpl(BidRepository bidRepository, SessionService sessionService, UserService userService, FilmService filmService, BlockService blockService) {
        this.bidRepository = bidRepository;
        this.sessionService = sessionService;
        this.userService = userService;
        this.filmService = filmService;
        this.blockService = blockService;
    }

    @Override
    @Transactional
    public BidInfoDto createBidOnAddUser(BidDto bidDto) {
        Bid bid = new Bid();
        bid.setBidType(BidType.ADD_USER_TO_SESSION);
        bid.setStatus(Status.OPEN);
        bid.setUser(userService.getUserIdFromSecurityContext());
        bid.setSession(sessionService.findSessionById(bidDto.getSessionId()));
        log.info("Bid was made");

        bidRepository.save(bid);

        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public BidInfoDto approveBidOnAddUser(long id) {
        User sessionCreator = userService.getUserIdFromSecurityContext();
        Bid bid = getBidById(id);

        if (sessionCreator.getId().equals(bid.getSession().getCreator().getId())) {
            if (bid.getSession().getUsers().contains(bid.getUser())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in this session");
            }

            bid.setStatus(Status.APPROVED);
            bidRepository.save(bid);

            User user = bid.getUser();
            Session session = bid.getSession();
            session.getUsers().add(user);
            sessionService.saveSession(session);
            log.info("Bid was approved");
        } else {
            log.info("You are not creator of session.");
        }

        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public BidInfoDto rejectBidOnAddUser(long id) {
        User sessionCreator = userService.getUserIdFromSecurityContext();
        Bid bid = getBidById(id);

        if (sessionCreator.getId().equals(bid.getSession().getCreator().getId())) {
            bid.setStatus(Status.REJECTED);
            bidRepository.save(bid);
            log.info("Bid was rejected");
        } else {
            log.info("You are not creator of session.");
        }

        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public BidInfoDto createBidOnDeleteFilm(BidDeleteFilmDto bidDeleteFilmDto) {
        User user = userService.getUserIdFromSecurityContext();
        Film film = filmService.findFilmById(bidDeleteFilmDto.getFilmId());

        Bid bid = new Bid();
        bid.setBidType(BidType.DELETE_FILM);
        bid.setStatus(Status.OPEN);
        bid.setUser(user);
        bid.setTargetFilm(film);
        bidRepository.save(bid);

        Block block = blockService.findById(bidDeleteFilmDto.getBlockId());
        film.getBlockSet().add(block);
        filmService.saveFilm(film);

        log.info("Bid for deleting film created successfully");
        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public BidInfoDto approveBidOnDeleteFilm(long bidId) {
        Bid bid = getBidById(bidId);
        bid.setStatus(Status.APPROVED);
        bidRepository.save(bid);

        Film film = bid.getTargetFilm();
        film.setIs_blocked(true);
        filmService.saveFilm(film);

        log.info("Bid for deleting film approved successfully");
        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public BidInfoDto createBidOnCancelSession(long id) {
        User user = userService.getUserIdFromSecurityContext();
        Session session = sessionService.findSessionById(id);

        Bid bid = new Bid();
        bid.setBidType(BidType.DELETE_SESSION);
        bid.setStatus(Status.OPEN);
        bid.setUser(user);
        bid.setSession(session);
        bidRepository.save(bid);

        log.info("Bid for deleting session created successfully");
        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public BidInfoDto approveBidOnCancelSession(long id) {
        Bid bid = getBidById(id);
        Session session = sessionService.findSessionById(bid.getSession().getId());
        session.setIsCanceled(true);
        sessionService.saveSession(session);

        bid.setStatus(Status.APPROVED);
        bidRepository.save(bid);

        log.info("Bid for deleting session approved successfully");
        return mapBidToBidInfoDto(bid);
    }

    @Override
    @Transactional
    public void createBidOnBlockUser(UserDto userDto) {
        User user = userService.findByUsername(userDto.getUsername());

        Bid bid = new Bid();
        bid.setBidType(BidType.DELETE_USER);
        bid.setStatus(Status.OPEN);
        bid.setUser(user);
        bidRepository.save(bid);

        log.info("Bid for blocking user created successfully");
    }

    @Override
    @Transactional
    public void approveBidOnBlockUser(long id) {
        Bid bid = getBidById(id);
        User user = bid.getUser();
        user.setIsBlocked(true);
        userService.saveUser(user);

        bid.setStatus(Status.APPROVED);
        bidRepository.save(bid);

        log.info("Bid for blocking user approved successfully");
    }

    @Override
    @Transactional
    public BidInfoDto rejectBidByModerator(long id) {
        Bid bid = getBidById(id);
        bid.setStatus(Status.REJECTED);
        bidRepository.save(bid);

        log.info("Bid was rejected");
        return mapBidToBidInfoDto(bid);
    }

    private BidInfoDto mapBidToBidInfoDto(Bid bid) {
        Session session = bid.getSession();
        Film film = session != null ? session.getFilmId() : bid.getTargetFilm();
        long sessionId = session != null ? session.getId() : 0L;
        return new BidInfoDto(
                bid.getId(),
                sessionId,
                bid.getUser().getUsername(),
                film,
                bid.getUser().getIsBlocked(),
                bid.getStatus(),
                bid.getBidType()
        );
    }

    @Override
    public List<BidInfoDto> getAllBids() {
        return bidRepository.findAll().stream()
                .map(this::mapBidToBidInfoDto)
                .collect(Collectors.toList());
    }

    private Bid getBidById(long id) {
        return bidRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bid not found"));
    }
}
