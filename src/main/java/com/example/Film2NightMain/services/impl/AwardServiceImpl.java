package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.dto.AwardDto;
import com.example.Film2NightMain.dto.AwardSessionDto;
import com.example.Film2NightMain.entities.Award;
import com.example.Film2NightMain.entities.Session;
import com.example.Film2NightMain.entities.User;
import com.example.Film2NightMain.repositories.AwardRepository;
import com.example.Film2NightMain.services.AwardService;
import com.example.Film2NightMain.services.SessionService;
import com.example.Film2NightMain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;
    private final SessionService sessionService;
    private final UserService userService;

    @Override
    public AwardSessionDto setAwardToSession(AwardDto awardDto) {
        Long sessionId = awardDto.getSessionId();
        Long awardId = awardDto.getAwardId();

        Session session = sessionService.findSessionById(sessionId);
        Award award = awardRepository.findById(awardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Award not found"));

        User creator = session.getCreator();
        creator.getAwards().add(award);

        userService.saveUser(creator);

        AwardSessionDto awardSessionDto = new AwardSessionDto();
        awardSessionDto.setSessionId(session.getId());
        awardSessionDto.setSessionDate(session.getStartTime());
        awardSessionDto.setAwardId(award.getId());
        awardSessionDto.setAwardName(award.getName());

        return awardSessionDto;
    }
}
