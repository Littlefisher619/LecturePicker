package com.littlefisher.LecturePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String usr="",pwd="",email="",code="";
    private static boolean NOMAIL=false;
    private static int interval=180;
    private static void loadConfigFile(){
        DataManager config=new DataManager("config.txt");
        List<String> dataList=config.getDataList();

        if(!NOMAIL) {
            if (dataList.size() < 4) {
                msgUtil.failure("File format error! You should write username,password,email and connection code LINE BY LINE!");
                System.exit(0);
            } else {
                usr = dataList.get(0);
                pwd = dataList.get(1);
                email = dataList.get(2);
                code = dataList.get(3);
            }
        }else{
            if (dataList.size() < 2) {
                msgUtil.failure("File format error! You should write username,password LINE BY LINE!");
                System.exit(0);
            } else {
                usr = dataList.get(0);
                pwd = dataList.get(1);
            }
        }
    }
    private static DetectorTask parseArgs(String[] args)
    {
        String _arg=String.join(" ",args).toLowerCase();
        if(_arg.contains("debug")){
            msgUtil.toggleDebugOn();
            msgUtil.debug("Debug mode is enabled!");
        }

        if(_arg.contains("nomail")) {
            NOMAIL=true;
            msgUtil.warning("You turn off the mail notification service!");
        }else{
            NOMAIL=false;
            msgUtil.info("Mail notification service is enabled!");
        }

        if(_arg.contains("i=")){
            String numtmp= _arg.substring(_arg.indexOf("i=")+2).split(" ")[0];
            try{
                interval=Integer.parseInt(numtmp);
                msgUtil.info("You change interval to %d second(s).",interval);
                if (interval<10){
                    msgUtil.failure("You can't set interval smaller than 10s!");
                    System.exit(-1);
                }else if(interval<=30) {
                    msgUtil.warning("Your interval is quite small,be careful for admins!");
                }
            }catch (NumberFormatException e) {
                msgUtil.failure("Fail to parse interval \"%s\" to int!",numtmp);
                System.exit(-1);
            }
        }else{
            msgUtil.info("Interval is set to 180s by default.");
        }

        loadConfigFile();
        DetectorTask detectorTask=new DetectorTask(usr,pwd);

        if(!NOMAIL) detectorTask.initMailService(email,code);

        if(_arg.contains("whitelist")){
            msgUtil.info("whitelist mode is enabled!");
            detectorTask.WHITELISTMODE=true;
        }else{
            msgUtil.info("blacklist mode is enabled by default!");
            detectorTask.WHITELISTMODE=false;
        }

        return detectorTask;
    }
    public static void main(String[] args) {

        msgUtil.init();
        System.out.println("LecturePicker Start!");

        DetectorTask detectorTask=parseArgs(args);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(detectorTask,1,interval, TimeUnit.SECONDS);


        commander();
    }
    public static void commander() {
        Scanner s=new Scanner(System.in);
        while (true)
        {
            String str=s.nextLine().trim().toLowerCase();
            if(str.isEmpty()) continue;

            if(str.equals("ping"))
                System.out.println("pong! Program is alive!");
        }
    }


}
