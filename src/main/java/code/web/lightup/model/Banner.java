package code.web.lightup.model;

import java.time.LocalDate;

public class Banner {
    private int id;
    private String title;
    private String img;
    private String position;
    private String link_banner;
    private String status;
    private int display_number;
    private LocalDate start_date;
    private LocalDate end_date;

    public Banner() {
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getImg(){
        return img;
    }
    public void setImg(String img){
        this.img = img;
    }

    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }
    public String getLink_banner(){
        return link_banner;
    }
    public void setLink_banner(String link_banner){
        this.link_banner = link_banner;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public int getDisplay_number(){
        return display_number;
    }
    public void setDisplay_number(int display_number){
        this.display_number = display_number;
    }
    public LocalDate getStart_date(){
        return start_date;
    }
    public void setStart_date(LocalDate start_date){
        this.start_date = start_date;
    }
    public LocalDate getEnd_date(){
        return end_date;
    }
    public void setEnd_date(LocalDate end_date){
        this.end_date = end_date;
    }









}
