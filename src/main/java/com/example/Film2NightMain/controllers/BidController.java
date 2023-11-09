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
@Api(tags = "Bids")
public class BidController {
    private final BidServiceImpl bidService;

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/addUser/create")
    @ApiOperation("Create a request to participate in a session")
    public ResponseEntity<BidInfoDto> createBidOnSessionView(@RequestBody BidDto bidDto) {
        return ResponseEntity.ok().body(bidService.createBidOnAddUser(bidDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/addUser/create/approve/{id}")
    @ApiOperation("Approve the request to participate in the session")
    public ResponseEntity<BidInfoDto> approveBidOnSessionView(@PathVariable Long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnAddUser(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/addUser/create/reject/{id}")
    @ApiOperation("Decline a session request")
    public ResponseEntity<BidInfoDto> rejectBidOnSessionView(@PathVariable Long id) {
        return ResponseEntity.ok().body(bidService.rejectBidOnAddUser(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/delete/{id}")
    @ApiOperation("Create a request to delete a session")
    public ResponseEntity<BidInfoDto> createBidOnSessionCancel(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.createBidOnCancelSession(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/session/delete/approve/{id}")
    @ApiOperation("Approve the request to delete a session")
    public ResponseEntity<BidInfoDto> approveBidOnCancelSession(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnCancelSession(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/film/delete")
    @ApiOperation("Create a request to remove a film")
    public ResponseEntity<BidInfoDto> createBidOnDeleteFilm(@RequestBody BidDeleteFilmDto bidDeleteFilmDto) {
        return ResponseEntity.ok().body(bidService.createBidOnDeleteFilm(bidDeleteFilmDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/film/delete/approve/{id}")
    @ApiOperation("Approve the request to remove the film")
    public ResponseEntity<BidInfoDto> approveBidOnDeleteFilm(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnDeleteFilm(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/user/block")
    @ApiOperation("Create a request to delete a user")
    public ResponseEntity<BidDeleteUserDto> createBidOnBlockUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(bidService.createBidOnBlockUser(userDto));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/user/block/approve/{id}")
    @ApiOperation("Approve the request to delete a user")
    public ResponseEntity<BidDeleteUserDto> approveBidOnBlockUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.approveBidOnBlockUser(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping("/reject/{id}")
    @ApiOperation("Reject request")
    public ResponseEntity<BidInfoDto> rejectBidByModerator(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(bidService.rejectBidByModerator(id));
    }
}
