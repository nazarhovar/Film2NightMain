package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.*;
import com.example.Film2NightMain.entities.*;
import com.example.Film2NightMain.repositories.BidRepository;
import com.example.Film2NightMain.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final SessionService sessionService;
    private final UserService userService;
    private final FilmService filmService;
    private final BlockService blockService;

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

    public BidInfoDto createBidOnDeleteFilm(BidDeleteFilmDto bidDeleteFilmDto) {
        User user = userService.getUserIdFromSecurityContext();
        Session session = new Session();
        session.setFilmId(filmService.findFilmById(bidDeleteFilmDto.getFilmId()));
        session.setIsCanceled(true);
        sessionService.saveSession(session);

        Bid bid = new Bid();
        bid.setBidType(BidType.DELETE_FILM);
        bid.setStatus(Status.OPEN);
        bid.setUser(user);
        bid.setSession(session);
        bidRepository.save(bid);

        Film film = session.getFilmId();
        Block block = blockService.findById(bidDeleteFilmDto.getBlockId());
        film.getBlockSet().add(block);
        filmService.saveFilm(film);

        log.info("Bid for deleting film created successfully");
        return mapBidToBidInfoDto(bid);
    }


    public BidInfoDto approveBidOnDeleteFilm(long bidId) {
        Bid bid = getBidById(bidId);
        bid.setStatus(Status.APPROVED);
        bidRepository.save(bid);

        Film film = bid.getSession().getFilmId();
        film.setIs_blocked(true);
        filmService.saveFilm(film);

        log.info("Bid for deleting film approved successfully");
        return mapBidToBidInfoDto(bid);
    }

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

    public BidDeleteUserDto createBidOnBlockUser(UserDto userDto) {
        User user = userService.findByUsername(userDto.getUsername());
        Session session = new Session();
        session.setIsCanceled(true);
        sessionService.saveSession(session);

        Bid bid = new Bid();
        bid.setBidType(BidType.DELETE_USER);
        bid.setStatus(Status.OPEN);
        bid.setUser(user);
        bid.setSession(session);
        bidRepository.save(bid);

        BidInfoDto bidInfoDto = mapBidToBidInfoDto(bid);

        log.info("Bid for blocking user created successfully");
        return createBidDeleteUserDto(bidInfoDto, user);
    }

    public BidDeleteUserDto approveBidOnBlockUser(long id) {
        Bid bid = getBidById(id);
        User user = bid.getUser();
        user.setIsBlocked(true);
        userService.saveUser(user);

        bid.setStatus(Status.APPROVED);
        bidRepository.save(bid);

        BidInfoDto bidInfoDto = mapBidToBidInfoDto(bid);

        log.info("Bid for blocking user approved successfully");
        return createBidDeleteUserDto(bidInfoDto, user);
    }

    public BidInfoDto rejectBidByModerator(long id) {
        Bid bid = getBidById(id);
        bid.setStatus(Status.REJECTED);
        bidRepository.save(bid);

        log.info("Bid was rejected");
        return mapBidToBidInfoDto(bid);
    }

    private BidInfoDto mapBidToBidInfoDto(Bid bid) {
        return new BidInfoDto(
                bid.getId(),
                bid.getSession().getId(),
                bid.getUser().getUsername(),
                bid.getSession().getFilmId(),
                bid.getUser().getIsBlocked(),
                bid.getStatus(),
                bid.getBidType()
        );
    }

    private BidDeleteUserDto createBidDeleteUserDto(BidInfoDto bidInfoDto, User user) {
        return new BidDeleteUserDto(
                bidInfoDto.getBidId(),
                bidInfoDto.getUsername(),
                user.getIsBlocked(),
                bidInfoDto.getStatusType().toString(),
                bidInfoDto.getBidType().toString()
        );
    }

    private Bid getBidById(long id) {
        return bidRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bid not found"));
    }
}
