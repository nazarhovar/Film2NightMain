package com.example.Film2NightMain.controllers;

import com.example.Film2NightMain.dto.*;
import com.example.Film2NightMain.services.impl.BidServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bid")
@Api(tags = "Заявки")
public class BidController {
    private final BidServiceImpl bidService;

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/addUser/create")
    @ApiOperation("Создать заявку на участие в сеансе")
    public ResponseEntity<BidInfoDto> createBidOnSessionView(@RequestBody BidDto bidDto) {
        return ResponseEntity.ok().body(bidService.createBidOnAddUser(bidDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/addUser/create/approve/{id}")
    @ApiOperation("Одобрить заявку на участие в сеансе")
    public ResponseEntity<BidInfoDto> approveBidOnSessionView(@PathVariable Long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnAddUser(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/addUser/create/reject/{id}")
    @ApiOperation("Отклонить заявку на участие в сеансе")
    public ResponseEntity<BidInfoDto> rejectBidOnSessionView(@PathVariable Long id) {
        return ResponseEntity.ok().body(bidService.rejectBidOnAddUser(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/delete/{id}")
    @ApiOperation("Создать заявку на удаление сессии")
    public ResponseEntity<BidInfoDto> createBidOnSessionCancel(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.createBidOnCancelSession(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/delete/approve/{id}")
    @ApiOperation("Одобрить заявку на удаление сессии")
    public ResponseEntity<BidInfoDto> approveBidOnCancelSession(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnCancelSession(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/film/delete")
    @ApiOperation("Создать заявку на удаление фильма")
    public ResponseEntity<BidInfoDto> createBidOnDeleteFilm(@RequestBody BidDeleteFilmDto bidDeleteFilmDto) {
        return ResponseEntity.ok().body(bidService.createBidOnDeleteFilm(bidDeleteFilmDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/film/delete/approve/{id}")
    @ApiOperation("Одобрить заявку на удаление фильма")
    public ResponseEntity<BidInfoDto> approveBidOnDeleteFilm(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnDeleteFilm(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/user/block")
    @ApiOperation("Создать заявку на удаление юзера")
    public ResponseEntity<BidDeleteUserDto> createBidOnBlockUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(bidService.createBidOnBlockUser(userDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/user/block/approve/{id}")
    @ApiOperation("Одобрить заявку на удаление юзера")
    public ResponseEntity<BidDeleteUserDto> approveBidOnBlockUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnBlockUser(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/reject/{id}")
    @ApiOperation("Отклонить заявку")
    public ResponseEntity<BidInfoDto> rejectBidByModerator(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.rejectBidByModerator(id));
    }
}
