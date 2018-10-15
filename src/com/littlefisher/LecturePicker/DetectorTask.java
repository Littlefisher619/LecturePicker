package com.littlefisher.LecturePicker;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.littlefisher.LecturePicker.LectureData.Lecture;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;

public class DetectorTask  extends TimerTask {
    private String username,password,id;
    private Mail qqmail;
    private WebClient webClient;
    private static PrintStream out=System.out;
    private DataManager record=new DataManager("record.txt");
    private DataManager keyword=new DataManager("keyword.txt");
    public boolean WHITELISTMODE=false;

    DetectorTask(String username,String password){
        msgUtil.info("Initialize scheduled task...");
        this.username = username;
        this.password = password;

        msgUtil.info("Initialize webclient...");
        this.initWebClient();

        msgUtil.info("Initialize finished...");
    }

    public void run(){
        msgUtil.newline(2);
        msgUtil.info(new Date() + " - Scheduled Task Start Up!");
        initWebClient();
        boolean status=login();
        if(status){
            List<Lecture> lectureList=downloadAndParseLectureData();
            if(lectureList!=null) doAttendTask(lectureList);
        }
        webClient.close();
        msgUtil.info("Scheduled Task Finish!");
    }

    public void initMailService(String mail,String mailpwd){
        msgUtil.info("Initialize mail notification service...");
        this.qqmail=new Mail(mail,mailpwd);
        try {
            qqmail.SendMail("LecturePicker service is up!", "Congratulations! I will now help you sign up for these lectures!");
            msgUtil.info("Mail service up!");
        }catch (Exception e){
            msgUtil.info("Mail service unavailable...");
            qqmail=null;
            e.printStackTrace(out);
        }
    }
    private void initWebClient(){
        webClient=new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
    }

    private WebResponse sendGetRequest(String url)
    {
        try{
            return webClient.getWebConnection().getResponse(new WebRequest(new URL(url), HttpMethod.GET));
        }catch (Exception e){
            out.printf("Request %s Failed\n",url);
            e.printStackTrace(out);
        }
        return null;
    }
    /*
     * @Description:
     * @Return: Fail - false ,Succeed - true
     */
    private boolean login(){
        msgUtil.info("Login to http://jwch.fzu.edu.cn/ ...");
        HtmlPage p;
        try {
            p = webClient.getPage("http://jwch.fzu.edu.cn/");
        }catch (Exception e){
            msgUtil.failure("Failed while open http://jwch.fzu.edu.cn/");
            e.printStackTrace(out);
            return false;
        }

        HtmlTextInput txtUserName= p.getFirstByXPath("//*[@id=\"muser\"]");
        HtmlPasswordInput txtPassword= p.getFirstByXPath("//*[@id=\"passwd\"]");
        HtmlImageInput btnSubmit=p.getFirstByXPath("//*[@id=\"loginform\"]/table/tbody/tr/td[2]/div/input");
        txtUserName.setValueAttribute(username);
        txtPassword.setValueAttribute(password);

        try {
            p=(HtmlPage)btnSubmit.click();
        } catch(Exception e){
            msgUtil.failure("Failed while click submit button!");
            e.printStackTrace(out);
            return false;
        }
        id=p.getBaseURI().split("id=")[1];
        msgUtil.success("Login successfully...");
        return true;

    }
    private void attendLecture(Lecture l){

        for(HtmlElement element: l.getOperation().getHtmlElementDescendants())
        {
            if(element.getAttribute("value").equals("确定报名"))
            {

                try {
                    //DEBUG - if(true) throw new IOException("");
                    element.click();
                    msgUtil.success("Sign up for %s successfully.\n",l.getName());
                    if(qqmail!=null) qqmail.SendMail( String.format("Sign up for %s - %s successfully!",l.getType(),l.getName())
                            ,String.format("Lecture will be held at %s in %s , Author: %s<br>Spared seat(s): %s，Signup-deadline: %s<br><br><a href=\"http://jwch.fzu.edu.cn\">Login cancel it!</a>",
                                    l.getStartTime(),l.getLocation(),l.getAuthor(),l.getLeftSeat(),l.getSignupTime()));

                }catch (IOException e){
                    msgUtil.failure("Sign up for %s failed!\n",l.getName());
                    try {
                        StringWriter errorWriter = new StringWriter();
                        PrintWriter pw = new PrintWriter(errorWriter);
                        e.printStackTrace(pw);
                        //Put all stacktrace to a string
                        if (qqmail != null) qqmail.SendMail(String.format("FAILED! %s - %s SIGN UP FAILED", l.getType(), l.getName())
                                , String.format("Sign up for this lecture failed, plz sign up manually！<a href=\"http://jwch.fzu.edu.cn\">Login to JWC</a><br>StackTrace:<br>%s<br><br>Lecture will be held at %s in %s , Author: %s<br>Spared seat(s): %s，Signup-deadline: %s<br><br>",
                                        errorWriter.toString().replaceAll("\n","<br>"),l.getStartTime(), l.getLocation(), l.getAuthor(), l.getLeftSeat(), l.getSignupTime()));
                    }catch (MessagingException e1) {
                        msgUtil.failure("Send failure notify mail failed: %s\n",l.getName());
                        e1.printStackTrace(out);
                    }
                }catch (MessagingException e) {
                    msgUtil.failure("Send success notify mail failed: %s\n",l.getName());
                    e.printStackTrace(out);
                }
                break;
            }


        }
    }

    public void doAttendTask(List<Lecture> lectures){
        for(int i=0;i<lectures.size();i++){
            Lecture l=lectures.get(i);
            out.printf("[%d]" + l.getDescription() + "\n",i);

            if(record.contains(l.getName())){
                msgUtil.warning("Skipping because already registered...");
                continue;
            }

            if(WHITELISTMODE){
                String rule=keyword.matches(l.getName());

                if(rule!=null) {
                    msgUtil.info("Whitelist Hit! OK! - \"%s\"!\n",rule);
                }
                else {
                    msgUtil.info("Whitelist Skip!");
                    continue;
                }
            }else{
                //BLACKLISTMODE
                String rule=keyword.matches(l.getName());

                if(rule!=null) {
                    msgUtil.info("Blacklist Hit! Skip it! - \"%s\"\n",rule);
                    continue;
                }
                else {
                    msgUtil.info("Blacklist Check OK!");
                }
            }

            switch (l.getStatus()){
                case WAITFORREGISTER:
                    msgUtil.info("This lecture is available!");
                    attendLecture(l);
                    record.appendStrIfNotExist(l.getName());
                    break;
                case REGISTERED:
                    msgUtil.info("This lecture has registered yet!");
                    record.appendStrIfNotExist(l.getName());
                    break;
                case NOSEAT:
                    msgUtil.warning("Unfortunately,this lecture is FULL,but program will check it later...");
                    break;
            }
        }
    }
    public List<Lecture> downloadAndParseLectureData(){
        List<Lecture> lectures=new ArrayList<Lecture>();
        msgUtil.info("Downloading lecture data...");
        HtmlPage p;
        try {
            p = webClient.getPage("http://59.77.226.35/student/glbm/lecture/jxjt_cszt.aspx?id=" + id);
        }catch (Exception e) {
            msgUtil.failure("Failed while download lecture data!");
            e.printStackTrace(out);
            return null;
        }

        HtmlTable tableCanJoin=p.getFirstByXPath("//*[@id=\"ContentPlaceHolder1_DataList_mt\"]");
        for(HtmlTableRow row:tableCanJoin.getRows()){
            List<HtmlTableCell> cells=row.getCells();
            if(cells.size()<=3 || cells.get(1).asText().equals("讲座类别")) continue;

            lectures.add(new Lecture(
                    cells.get(4).asText().trim().replaceAll("\\s+"," "),
                    cells.get(3).asText().trim().replaceAll("\\s+"," "),
                    cells.get(6).asText().trim().replaceAll("\\s+"," "),
                    cells.get(7).asText().trim().replaceAll("\\s+"," ").replaceAll("：",":"),
                    cells.get(5).asText().trim().replaceAll("\\s+"," ").replaceAll("：",":"),
                    cells.get(1).asText().trim().replaceAll("\\s+"," "),
                    Integer.parseInt(cells.get(8).asText().trim().replaceAll("\\s+"," ")),
                    cells.get(0),
                    cells.get(0).asText()
            ));
        }
        msgUtil.success("Finish! %d lectures dumped.\n",lectures.size());
        return lectures;
    }
}
