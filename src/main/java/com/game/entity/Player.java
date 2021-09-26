package com.game.entity;


import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String name;
    @Column
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    @Column
    private Integer experience;
    @Column
    private Integer level;
    @Column
    private Integer untilNextLevel;
    @Column
    private Date birthday;
    @Column
    private Boolean banned;



    public Player() {}

    public Player(String name, String title, Race race, Profession profession, Date birthday, Boolean banned,
                  Integer experience, Integer level, Integer untilNextLevel) {

        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name) &&
                Objects.equals(title, player.title) &&
                race == player.race &&
                profession == player.profession &&
                Objects.equals(experience, player.experience) &&
                Objects.equals(level, player.level) &&
                Objects.equals(untilNextLevel, player.untilNextLevel) &&
                Objects.equals(birthday, player.birthday) &&
                Objects.equals(banned, player.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
    }

    @Override
    public String toString() {
//        LocalDate birthday = Instant.ofEpochMilli(this.birthday).atZone(ZoneId.systemDefault()).toLocalDate();
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer calculateUntilNextLevel() {
        return 50*(getLevel()+1)*(getLevel()+2)-getExperience();
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }
//
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean baned) {
        this.banned = baned;
    }

    public Integer calculateCurrentLevel(){
       return  (int)((Math.sqrt(2500 + 200*getExperience())-50)/100);
    }

    public Boolean checkNullFields(Player player){
        if(player==null) return false;
        return player.getName() != null && player.getTitle() != null && player.getRace() != null && player.getProfession() != null &&
                player.getExperience() != null && player.getBirthday() != null;
    }

    public Boolean checkNameLength(Player player){
        if(player==null) return false;
        if(player.getName()==null)return false;
        return player.getName().length() > 0 && player.getName().length() < 13;
//        return name.length() > 12 || name.matches("^\\s*$");

    }
    public Boolean checkTitleLength(Player player){
        if(player==null) return false;
        if(player.getTitle()==null)return false;
        return player.getTitle().length() > 0 && player.getTitle().length() < 31;

    }

    public Boolean checkNameNull(Player player){
        if(player==null) return false;
        return player.getName() == null;
    }

    public Boolean checkExperienceRange(Player player){
        if(player==null) return false;
        if(player.getExperience()==null) return false;
        return player.getExperience() > 0 && player.getExperience() < 10_000_000;
    }
     public Boolean checkNegativeBirthday(Player player){
         if(player==null) return false;
         if(player.getBirthday()==null)return false;
         long longBirthday = player.getBirthday().getTime();
         return longBirthday > 0;
     }

     public Boolean checkBirthdayRange(Player player){
        if(player==null) return false;
         if(player.getBirthday()==null)return false;
         long longBirthday = player.getBirthday().getTime();
         long begin = new GregorianCalendar(2000, Calendar.JANUARY,1).getTimeInMillis();
         long end = new GregorianCalendar(3000,Calendar.DECEMBER,31).getTimeInMillis();
        return longBirthday < end || longBirthday > begin;
     }

     public Boolean checkPossibilityToCreatePlayer(Player player){
        boolean nullField = checkNullFields(player);//add
        boolean nameLength = checkNameLength(player);//add
        boolean titleLength = checkTitleLength(player);//add
//        boolean nameNull = checkNameNull(player);
        boolean experienceRange = checkExperienceRange(player);//add
        boolean negativeBirthday = checkNegativeBirthday(player);//add
        boolean birthdayRange = checkBirthdayRange(player);//add
        return nullField && nameLength && titleLength &&
                experienceRange && negativeBirthday && birthdayRange;
     }

}
