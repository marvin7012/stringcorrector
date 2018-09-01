package com.company;

import java.io.*;
import java.util.ArrayList;



public class NGram {

    public static void main(String[] args) {

        String namesFile = "/Users/hannnnn/Desktop/knowledge_technologies/Assignment/kt_data/names.txt";
        String trainFile = "/Users/hannnnn/Desktop/knowledge_technologies/Assignment/kt_data/train.txt";
        String resultFile = "/Users/hannnnn/Desktop/knowledge_technologies/Assignment/kt_data/reslut_Global0.txt";
        int ngram = 2;
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
                float distance = 0;
                String[] train_ = lineTrain.toLowerCase().split("\\t");
                String _train_ = train_[0];
                readnames.mark(1000000000);
                while ((lineNames = readnames.readLine()) != null){
                    String names_ = lineNames.toLowerCase();
                    if (distance == getDistance(_train_, names_,ngram)){
                        list.add(names_);
                    }else if(distance < getDistance(_train_,names_,ngram)){
                        distance = getDistance(_train_,names_,ngram);
                        list.clear();
                        list.add(names_);
                    }
                }
                readnames.reset();
                result_in.write(_train_+":");
                //System.out.print(_train_+":");
                for (int i = 0; i <list.size(); i++){
//                    System.err.print(list.get(i)+" ");
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
        System.out.println(startMili-endMili);

    }


    //calculate the distance
    public static float getDistance(String source, String target, int n) {
        final int sl = source.length();
        final int tl = target.length();

        if (sl == 0 || tl == 0) {
            if (sl == tl) {
                return 1;
            }
            else {
                return 0;
            }
        }
        int cost = 0;
        if (sl < n || tl < n) {
            for (int i=0,ni=Math.min(sl,tl);i<ni;i++) {
                if (source.charAt(i) == target.charAt(i)) {
                    cost++;
                }
            }
            return (float) cost/Math.max(sl, tl);
        }
        char[] sa = new char[sl+n-1];
        float p[]; //'previous' cost array, horizontally
        float d[]; // cost array, horizontally
        float _d[]; //placeholder to assist in swapping p and d
        //construct sa with prefix
        for (int i=0;i<sa.length;i++) {
            if (i < n-1) {
                sa[i]=0; //add prefix
            }
            else {
                sa[i] = source.charAt(i-n+1);
            }
        }
        p = new float[sl+1];
        d = new float[sl+1];
        // indexes into strings s and t
        int i; // iterates through source
        int j; // iterates through target

        char[] t_j = new char[n]; // jth n-gram of t

        for (i = 0; i<=sl; i++) {
            p[i] = i;
        }

        for (j = 1; j<=tl; j++) {
            if (j < n) {
                for (int ti=0;ti<n-j;ti++) {
                    t_j[ti]=0;
                }
                for (int ti=n-j;ti<n;ti++) {
                    t_j[ti]=target.charAt(ti-(n-j));
                }
            }
            else {
                t_j = target.substring(j-n, j).toCharArray();
            }
            d[0] = j;
            for (i=1; i<=sl; i++) {
                cost = 0;
                int tn=n;
                for (int ni=0;ni<n;ni++) {
                    if (sa[i-1+ni] != t_j[ni]) {
                        cost++;
                    }
                    else if (sa[i-1+ni] == 0) { //discount matches on prefix
                        tn--;
                    }
                }
                float ec = (float) cost/tn;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+ec);
            }
            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return 1.0f - ((float) p[sl] / Math.max(tl, sl));
    }

}
