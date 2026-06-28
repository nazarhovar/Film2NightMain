package com.example.Film2NightMain.services;

import com.example.Film2NightMain.dto.*;
import java.util.List;

public interface BidService {
    BidInfoDto createBidOnAddUser(BidDto bidDto);

    BidInfoDto approveBidOnAddUser(long id);

    BidInfoDto rejectBidOnAddUser(long id);

    BidInfoDto createBidOnDeleteFilm(BidDeleteFilmDto bidDeleteFilmDto);

    BidInfoDto approveBidOnDeleteFilm(long bidId);

    BidInfoDto createBidOnCancelSession(long id);

    BidInfoDto approveBidOnCancelSession(long id);

    void createBidOnBlockUser(UserDto userDto);

    void approveBidOnBlockUser(long id);

    BidInfoDto rejectBidByModerator(long id);

    List<BidInfoDto> getAllBids();
}
