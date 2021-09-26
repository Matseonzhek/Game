package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PlayerService {
    List<Player> getSortedList(String name, String title, Race race, Profession profession, Long after, Long before,
                                      Boolean banned, Integer minExperience, Integer maxExperience,
                                      Integer minLevel, Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer size);
    Integer count(String name, String title, Race race, Profession profession, Long after, Long before,
                  Boolean banned, Integer minExperience, Integer maxExperience,
                  Integer minLevel, Integer maxLevel);
//    Player create(Player player);

    Player updatePlayer(Player dataForUpdatingPlayer, Player playerWhichMustBeUpdated);
    void delete(Long id);

    void saveAndUpdate(Player player);
    Player getById(Long id);


//    boolean existPlayer(Long id);
}
