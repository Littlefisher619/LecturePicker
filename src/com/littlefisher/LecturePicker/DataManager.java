package com.littlefisher.LecturePicker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static PrintStream out=System.out;
    private List<String> data=new ArrayList<>();
    private String filename;
    public DataManager(String filename){
        this.filename=filename;
        readRecordToList();
    }
    public List<String> getDataList(){return data;}
    public String getData(int i){return data.get(i);}
    public void appendStrIfNotExist(String s){

        if(!data.contains(s)) {
            data.add(s);
            try {
                saveRecordToFile();
            }catch (Exception e){
                msgUtil.failure("Fail to save %s!\n",filename);
            }
        }
    }
    public boolean contains(String s){
         return data.contains(s);
    }
    public String matches(String target){

        target=target.trim();
        for (String str:data) {
            msgUtil.debug("\"%s\".contains\"%s\" -> %s",target,str,target.contains(str));
            if(target.contains(str)){
                return str;
            }
        }
        return null;
    }
    public void saveRecordToFile() throws  Exception{


        File fre=new File(filename);
        if(!fre.exists())
        {
            try {
                msgUtil.warning("File %s doesn't exist! Generate it!\n",filename);
                fre.createNewFile();
            } catch (IOException e) {
                msgUtil.failure("File %s generate failed!\n",filename);
                e.printStackTrace(out);
            }
        }


        Writer writer=null;
        try {
            writer= new BufferedWriter( new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
            for(String s:data) {
                writer.write(s);
                writer.write("\n");
            }
            msgUtil.debug("File %s saved (%d records).",filename,data.size());
            msgUtil.debug(data.toString() );
        } catch (Exception e) {
            msgUtil.failure("FAIL to write %s\n",filename);
            e.printStackTrace(out);
        }finally{
            if(writer!=null) writer.close();
        }
    }
    public void readRecordToList(){
        data.clear();
        try{
            BufferedReader in =new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;
            while((line = in.readLine()) != null)
            {
                line=line.trim();
                if(line.isEmpty()) continue;
                data.add(line);
            }
            in.close();
        }catch (IOException e) {
            e.printStackTrace();
            msgUtil.failure("FAIL to load %s\n",filename);
        }
        msgUtil.debug("Read total %d records in file %s.",data.size(),filename);
        msgUtil.debug( data.toString() );
    }
}
