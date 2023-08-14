package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.*;

public interface BidService {
    BidInfoDto createBidOnAddUser(BidDto bidDto);

    BidInfoDto approveBidOnAddUser(long id);

    BidInfoDto rejectBidOnAddUser(long id);

    BidInfoDto createBidOnDeleteFilm(BidDeleteFilmDto bidDeleteFilmDto);

    BidInfoDto approveBidOnDeleteFilm(long bidId);

    BidInfoDto createBidOnCancelSession(long id);

    BidInfoDto approveBidOnCancelSession(long id);

    BidDeleteUserDto createBidOnBlockUser(UserDto userDto);

    BidDeleteUserDto approveBidOnBlockUser(long id);

    BidInfoDto rejectBidByModerator(long id);
}
