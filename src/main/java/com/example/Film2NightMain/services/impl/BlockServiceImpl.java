package com.example.Film2NightMain.services.impl;

import com.example.Film2NightMain.entities.Block;
import com.example.Film2NightMain.repositories.BlockRepository;
import com.example.Film2NightMain.services.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {
    private final BlockRepository blockRepository;

    @Override
    public Block findById(long id) {
        return blockRepository.findById(id).orElseThrow(() -> new RuntimeException("Block not found"));
    }
}
