package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    // 1. Get players list
    @GetMapping("")
    public ResponseEntity<List<Player>> getPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false) PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize

    ){
        final List<Player> playerList = playerService.getSortedList(name,title,race,profession,after,before,banned,
                minExperience,maxExperience,minLevel,maxLevel,order,pageNumber,pageSize);


        return new ResponseEntity<>(playerList,HttpStatus.OK);
    }

    // 2. Get players count
    @GetMapping("/count")
    public ResponseEntity<Integer> getCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ){
        Integer count = playerService.count(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

    // 3. Create player
    @PostMapping("/")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player){
        if(!player.checkPossibilityToCreatePlayer(player)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        playerService.saveAndUpdate(player);
        return new ResponseEntity<>(player,HttpStatus.OK);
    }

    // 4. Get player
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") String idString){

        if(checkIdNotNumber(idString)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        long id = Long.parseLong(idString);
        if(id==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player=playerService.getById(id);
        if(player==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player,HttpStatus.OK);
    }

    // 5. Update player
    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player dataForUpdatingPlayer, @PathVariable("id") String idString){
        if(checkIdNotNumber(idString)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        long id = Long.parseLong(idString);
        if(id==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player playerWhichMustBeUpdated = playerService.getById(id);
        if(playerWhichMustBeUpdated==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(dataForUpdatingPlayer.getName() == null && dataForUpdatingPlayer.getTitle() == null
                && dataForUpdatingPlayer.getRace() == null &&
                dataForUpdatingPlayer.getProfession() == null &&
                dataForUpdatingPlayer.getBirthday() == null && dataForUpdatingPlayer.getBanned() == null
                && dataForUpdatingPlayer.getExperience() == null) {
            dataForUpdatingPlayer = null;
            return new ResponseEntity<>(playerWhichMustBeUpdated,HttpStatus.OK);
        }
//        if(!dataForUpdatingPlayer.checkPossibilityToUpdatePlayer(dataForUpdatingPlayer)){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        if(dataForUpdatingPlayer.getExperience()!=null){
            boolean experienceRange = dataForUpdatingPlayer.getExperience() > 0
                    && dataForUpdatingPlayer.getExperience() < 10_000_000;//add
            if(!experienceRange){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        }
        if(dataForUpdatingPlayer.getBirthday()!=null){
            long begin = new GregorianCalendar(2000, Calendar.JANUARY,1).getTimeInMillis();
            long end = new GregorianCalendar(3000,Calendar.DECEMBER,31).getTimeInMillis();
            boolean birthdayRange = dataForUpdatingPlayer.getBirthday().getTime() < end
                    && dataForUpdatingPlayer.getBirthday().getTime() > begin
                    && dataForUpdatingPlayer.getBirthday().getTime() > 0;//add
            if(!birthdayRange) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        playerWhichMustBeUpdated =  playerService.updatePlayer(playerWhichMustBeUpdated,dataForUpdatingPlayer);
        return new ResponseEntity<>(playerWhichMustBeUpdated,HttpStatus.OK);
    }
//
    // 6. Delete player
    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") String idString){
        if(checkIdNotNumber(idString)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        long id = Long.parseLong(idString);
        if(id==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player=playerService.getById(id);
        if(player==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    public Boolean checkIdNotNumber(String idString) {
        long id=0;
        try {
            id = Long.parseLong(idString);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
}

