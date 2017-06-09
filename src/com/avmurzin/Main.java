package com.avmurzin;

import java.util.*;

public class Main {

    private static final Integer TREE_AMOUNT = 1;
    private static final Integer BRANCH_AMOUNT = 100;
    private static final Integer NEST_AMOUNT = 10000;
    // т.е. всего TREE_AMOUNT * BRANCH_AMOUNT * NEST_AMOUNT гнезд



    public static void main(String[] args) {
        HashMap<String, HashMap<String, HashMap<String, Nest>>> mapping = new HashMap<>();
        HashMap<String, HashMap<String, List<Nest>>> newMapping = new HashMap<>();
        ArrayList<ArrayList<List<Nest>>> arrayListMapping = new ArrayList<>();
        String treeId = "";

        for (int i = 0; i < TREE_AMOUNT; i++) {

            HashMap<String, HashMap<String, Nest>> branchMap = new HashMap<>();
            HashMap<String, List<Nest>> branchMapList = new HashMap<>();
            ArrayList<List<Nest>> branchList = new ArrayList<>();

            for (int j = 0; j < BRANCH_AMOUNT; j++) {

                HashMap<String, Nest> nestMap = new HashMap<>();
                ArrayList<Nest> nestList = new ArrayList<>();

                for (int k = 0; k < NEST_AMOUNT; k++) {
                    Nest nest = new Nest();
                    nestList.add(nest);
                    nestMap.put(UUID.randomUUID().toString(), nest);
                }
                branchMap.put(UUID.randomUUID().toString(), nestMap);
                branchMapList.put(UUID.randomUUID().toString(), nestList);
                branchList.add(nestList);
            }
            treeId = UUID.randomUUID().toString();
            mapping.put(treeId, branchMap);
            newMapping.put(treeId, branchMapList);
            arrayListMapping.add(branchList);
        }

        Forest forest = new Forest(mapping, newMapping, arrayListMapping);
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < BRANCH_AMOUNT; i++) {
            indexes.add(i);
        }

        long time;

        System.out.println("getNests");
        time = System.currentTimeMillis();
        forest.getNests(treeId, new ArrayList(mapping.get(treeId).keySet()));
        time = System.currentTimeMillis() - time;
        System.out.println(time);


        System.out.println("getNestsByStream");
        time = System.currentTimeMillis();
        forest.getNestsByStream(treeId, new ArrayList(mapping.get(treeId).keySet()));
        time = System.currentTimeMillis() - time;
        System.out.println(time);

        System.out.println("getNestsByStreamTwo");
        time = System.currentTimeMillis();
        forest.getNestsByStreamTwo(treeId, new ArrayList(mapping.get(treeId).keySet()));
        time = System.currentTimeMillis() - time;
        System.out.println(time);

        System.out.println("getNestsFromList");
        time = System.currentTimeMillis();
        forest.getNestsFromList(treeId, new ArrayList(newMapping.get(treeId).keySet()));
        time = System.currentTimeMillis() - time;
        System.out.println(time);


        System.out.println("getNestsFromIndexedTree");
        time = System.currentTimeMillis();
        forest.getNestsFromIndexedTree(0, indexes);
        time = System.currentTimeMillis() - time;
        System.out.println(time);
    }
}
