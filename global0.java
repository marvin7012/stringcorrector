package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by hannnnn on 2017/3/14.
 *
 * Global Edit Distance
 */
public class KT_Project_Global0 {

    public static void main(String [] args){
        String namesFile = "/Users/hannnnn/Desktop/knowledge_technologies/Assignment/kt_data/names.txt";
        String trainFile = "/Users/hannnnn/Desktop/knowledge_technologies/Assignment/kt_data/train.txt";
        String resultFile = "/Users/hannnnn/Desktop/knowledge_technologies/Assignment/kt_data/reslut_Global0.txt";
        int total = 0;
        int match_num = 0;
        int total_ = 0;
        long startMili=System.currentTimeMillis();

        try {
            InputStreamReader name_in = new InputStreamReader(new FileInputStream(new File(namesFile)));
            InputStreamReader train_in = new InputStreamReader(new FileInputStream(new File(trainFile)));
            FileWriter result_in = new FileWriter(new File(resultFile));

            BufferedReader readtrain = new BufferedReader(train_in);
            BufferedReader readnames = new BufferedReader(name_in);
            String lineTrain = null;
            String lineNames = null;
            while((lineTrain = readtrain.readLine()) != null){
                ArrayList<String> list = new ArrayList<String>();
                int distance = -100;
                String[] train_ = lineTrain.toLowerCase().split("\\t");
                String _train_ = train_[0];
                readnames.mark(1000000000);
                while ((lineNames = readnames.readLine()) != null){
                    String names_ = lineNames.toLowerCase();
                    if(distance == match(_train_, names_)){
                        list.add(names_);
                    }else if(distance < match(_train_, names_)){
                        distance = match(_train_, names_);
                        list.clear();
                        list.add(names_);
                    }
                }
                readnames.reset();
                result_in.write(_train_+":");
                //System.out.print(_train_+":");
                for (int i = 0; i <list.size(); i++){
                    //System.err.print(list.get(i)+" ");
                    result_in.write(list.get(i)+" ");
                    if(train_[1].equals(list.get(i))){
                        match_num += 1;
                        //System.err.println(match_num);
                    }

                }
                total += list.size();
                result_in.write("\n");
                total_ += 1;

            }
            result_in.write("total_match:"+total+"; correct_match:"+match_num+" ;precision:"+(double)match_num/total+"\n");
            result_in.write("match:"+total_+";correct:"+match_num+" ;recall:"+(double)match_num/total_);
            System.out.println("total_match:"+total+"; correct_match:"+match_num+" ;precision:"+(double)match_num/total);
            System.out.println("match:"+total_+";correct:"+match_num+" ;recall:"+(double)match_num/total_);
            name_in.close();
            train_in.close();
            result_in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endMili=System.currentTimeMillis();
        System.out.println(endMili-startMili);


    }
    //calculate the distance
    public static int match(String persian, String latin){
        char[] p = new char[persian.length()];
        char[] l = new char[latin.length()];
        p = persian.toCharArray();
        l = latin.toCharArray();
        //r parameter is [m,d,i,r]=[1,-1,-1,-1]
        int insertion = -1;
        int deletion = -1;
        int replace = -1;
        int equal = 1;
        int[][] arry = new int[latin.length()+1][persian.length()+1];
        arry[0][0] = 0;
        for (int i = 1; i <= latin.length(); i++) arry[i][0] = i * insertion;
        for (int j = 1; j <= persian.length(); j++) arry[0][j] = j * deletion;

        for (int i =1; i <= latin.length(); i++){
            for(int j = 1; j <= persian.length(); j++){
                if(l[i-1] == p[j-1]){
                    arry[i][j]=Math.max(Math.max(arry[i-1][j]+insertion, arry[i][j-1]+deletion),arry[i-1][j-1]+equal);
                }else {
                    arry[i][j]=Math.max(Math.max(arry[i-1][j]+insertion, arry[i][j-1]+deletion),arry[i-1][j-1]+replace);
                }
            }
        }
        return arry[latin.length()][persian.length()];
    }






}
