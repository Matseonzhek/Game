package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public List<Player> getSortedList(String name, String title, Race race, Profession profession, Long after, Long before,
                                      Boolean banned, Integer minExperience, Integer maxExperience,
                                      Integer minLevel, Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer size) {

        Pageable pageable = null;
        if (pageNumber==null){pageNumber=0;}
        if(size==null){size=3;}
            if(order==null){pageable= PageRequest.of(pageNumber,size, Sort.Direction.ASC,PlayerOrder.ID.getFieldName());}
            if(order!=null){pageable= PageRequest.of(pageNumber,size, Sort.Direction.ASC,order.getFieldName());}
        Page<Player> playerPage = playerRepository.findAll(findAccordingTerms(name, title,race,profession,after,before,banned,minExperience,maxExperience,
                minLevel,maxLevel),pageable);
        List<Player> playerList = playerPage.getContent();

        return playerList;
    }

    @Override
    public Integer count(String name, String title, Race race, Profession profession, Long after, Long before,
                         Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        List<Player> playerList = playerRepository.findAll(findAccordingTerms(name, title, race,profession,after,before,
                banned,minExperience,maxExperience,minLevel,maxLevel));
        return playerList.size();
    }


    @Override
    public Player getById(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        Player player = null;
//        try{
            if(optionalPlayer.isPresent()){
        player = optionalPlayer.get();}
//    }
//        catch (Exception e){//NOP
//             }
        return player;
//        return optionalPlayer.orElse(null);
    }

    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }
    public List<Player> getList(){
        return playerRepository.findAll();
    }

    // метод делает набор условий для формирования запроса в базу данных
    public Specification<Player> findAccordingTerms(String name, String title, Race race, Profession profession, Long after, Long before,
                                                    Boolean banned, Integer minExperience, Integer maxExperience,
                                                    Integer minLevel,Integer maxLevel){
        return new Specification<Player>() {
            @Override
            public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(cb.like(root.get("name"), "%"+ name + "%"));
                }
                if (title != null) {
                    predicates.add(cb.like(root.get("title"), "%"+ title + "%"));
                }
                if (race != null) {
                    predicates.add(cb.equal(root.get("race"), race));
                }
                if (profession != null) {
                    predicates.add(cb.equal(root.get("profession"), profession));
                }
                if (after != null && before != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("birthday"),  new Date(after)));
                }
                if(before != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("birthday"),new Date(before)));
                }
                if (banned != null) {
                    predicates.add(cb.equal(root.get("banned"), banned));
                }
                if(minExperience !=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("experience"), minExperience-1));
                }
                if(maxExperience !=null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("experience"), maxExperience+1));
                }
                if (minLevel != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("level"), minLevel));
                }
                if (maxLevel != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("level"),  maxLevel));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

    @Override
    public Player updatePlayer(Player playerWhichMustBeUpdated, Player  dataForUpdatingPlayer) {

        long begin = new GregorianCalendar(2000, Calendar.JANUARY,1).getTimeInMillis();
        long end = new GregorianCalendar(3000,Calendar.DECEMBER,31).getTimeInMillis();

        if(dataForUpdatingPlayer.getName()!=null){
            boolean nameLength = dataForUpdatingPlayer.getName().length() > 0
                    && dataForUpdatingPlayer.getName().length() < 13;//add
            if(nameLength) {
                playerWhichMustBeUpdated.setName(dataForUpdatingPlayer.getName());
            }
        }
        if(dataForUpdatingPlayer.getTitle()!=null ) {
            boolean titleLength = dataForUpdatingPlayer.getTitle().length() > 0
                    && dataForUpdatingPlayer.getTitle().length() < 31;//add
            if (titleLength) {
                playerWhichMustBeUpdated.setTitle(dataForUpdatingPlayer.getTitle());
            }
        }
        if(dataForUpdatingPlayer.getRace()!=null){
            playerWhichMustBeUpdated.setRace(dataForUpdatingPlayer.getRace());
        }
        if(!(dataForUpdatingPlayer.getProfession()==null)){
            playerWhichMustBeUpdated.setProfession(dataForUpdatingPlayer.getProfession());
        }
        if(dataForUpdatingPlayer.getExperience()!=null){
//            boolean experienceRange = dataForUpdatingPlayer.getExperience() > 0
//                    && dataForUpdatingPlayer.getExperience() < 10_000_000;//add
//            if(experienceRange){
            playerWhichMustBeUpdated.setExperience(dataForUpdatingPlayer.getExperience());
//        }
        }
        if(dataForUpdatingPlayer.getBirthday()!=null){
//            boolean birthdayRange = dataForUpdatingPlayer.getBirthday().getTime() < end
//                    && dataForUpdatingPlayer.getBirthday().getTime() > begin
//                    && dataForUpdatingPlayer.getBirthday().getTime() > 0;//add
//            if(birthdayRange) {
                playerWhichMustBeUpdated.setBirthday(dataForUpdatingPlayer.getBirthday());
//            }
        }
        if(!(dataForUpdatingPlayer.getBanned()==null)){
            playerWhichMustBeUpdated.setBanned(dataForUpdatingPlayer.getBanned());
        }
        playerWhichMustBeUpdated.setLevel(playerWhichMustBeUpdated.calculateCurrentLevel());
        playerWhichMustBeUpdated.setUntilNextLevel(playerWhichMustBeUpdated.calculateUntilNextLevel());

        return playerRepository.save(playerWhichMustBeUpdated);
    }


    @Override
    public void saveAndUpdate(Player player) {
        player.setLevel(player.calculateCurrentLevel());
        player.setUntilNextLevel(player.calculateUntilNextLevel());
        playerRepository.save(player);

    }
}
