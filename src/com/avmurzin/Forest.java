package com.avmurzin;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andrey V. Murzin on 09.06.17.
 */
public class Forest {
    private HashMap<String, HashMap<String, HashMap<String, Nest>>> mapping;

    public Forest(HashMap<String, HashMap<String, HashMap<String, Nest>>> mapping,
                  HashMap<String, HashMap<String, List<Nest>>> newMapping,
                  ArrayList<ArrayList<List<Nest>>> arrayListMapping) {
        this.mapping = mapping;
        this.newMapping = newMapping;
        this.arrayListMapping = arrayListMapping;
    }

    /**
     * Очевидный метод - перебор всех ветвей и получение в каждой коллекции значений.
     * k - число деревьев, n - число ветвей, m - число гнезд в каждой ветви
     * Худший случай операции получения элемента HashMap (например .get(branchId)) - O(n)
     * Получения произвольного элемента HashMap - O(1), соответственно всех элементов (.values()) - O(m)
     * Добавление элемента в конец LinkedList - O(1)
     * Итоговая сложность в силу вложенности операций - цикл перебора n ветвей (в максимуме, конечно,
     * минимально параметр branchId может содержать и 1 элемент), где в каждой итерации получение
     * элемента HashMap (ветви) из множества n, получение всех m этой ветви и добавление в выходной список m элементов -
     * O(k + (n * (n + 2 * m)))
     * Затраты памяти - структура result (размер Nest * n * m), структура tree (размер Nest * n * m),
     * структура для временного хранения содержимого tree.get(branchId).values() - (размер Nest * m)
     * @param treeId
     * @param branchIds
     * @return
     */
    public List<Nest> getNests(String treeId, List<String> branchIds) {

        if (treeId == null || branchIds == null || branchIds.isEmpty() || !mapping.containsKey(treeId)) {
            return Collections.emptyList();
        }

        //LinkedList - т.к. по мере роста списка не потребуется операция увеличения размера в отличие от ArrayList
        List<Nest> result = new LinkedList<>();

        //вынесено из цикла чтобы обеспечить (+ k) к сложности вместо (* k)
        HashMap<String, HashMap<String, Nest>> tree = mapping.get(treeId);

        for (String branchId : branchIds) {
            result.addAll(tree.get(branchId).values());
        }
        return result;
    }

    /**
     * В сущности то же самое - перебор всех ветвей и получение от каждой множества значений.
     * O(k + (n * (n + 2m)))
     * Затраты те же.
     * @param treeId
     * @param branchIds
     * @return
     */
    public List<Nest> getNestsByStream(String treeId, List<String> branchIds) {

        if (treeId == null || branchIds == null || branchIds.isEmpty() || !mapping.containsKey(treeId)) {
            return Collections.emptyList();
        }

        List<Nest> result = new LinkedList<>();
        HashMap<String, HashMap<String, Nest>> tree = mapping.get(treeId); //O(k)

        branchIds.stream().map((key) -> tree.get(key).values()).forEach((collection) -> result.addAll(collection));
        return result;
    }

    /**
     * Здесь ситуацию удалось ухудшить до O(k + n * (n + 3 * m))
     * @param treeId
     * @param branchIds
     * @return
     */
    public List<Nest> getNestsByStreamTwo(String treeId, List<String> branchIds) {

        if (treeId == null || branchIds == null || branchIds.isEmpty() || !mapping.containsKey(treeId)) {
            return Collections.emptyList();
        }

        List<Nest> result = new LinkedList<>();
        HashMap<String, HashMap<String, Nest>> tree = mapping.get(treeId);

        tree.entrySet().stream().filter((e) -> branchIds.contains(e.getKey())).map((e) -> e.getValue().values()).forEach((collection) -> result.addAll(collection));
        return result;
    }


    /**
     * Модифицированная структура для хранения гнезд (предполагаем, что Nest имеет втутри id, и маркировать его внешним
     * ключом String не требуется)
     */
    private HashMap<String, HashMap<String, List<Nest>>> newMapping;


    /**
     * Избавились от одной операции .values()
     * O(k + n * (n + m))
     * @param treeId
     * @param branchIds
     * @return
     */
    public List<Nest> getNestsFromList(String treeId, List<String> branchIds) {
        if (treeId == null || branchIds == null || branchIds.isEmpty() || !newMapping.containsKey(treeId)) {
            return Collections.emptyList();
        }

        List<Nest> result = new LinkedList<>();
        HashMap<String, List<Nest>> tree = newMapping.get(treeId);

        branchIds.stream().map((key) -> tree.get(key)).forEach((collection) -> result.addAll(collection));
        return result;
    }

    /**
     * Модифицированная структура для хранения гнезд. Предполагаем, что можно для маркировки деревьев и ветвей
     * использовать не строки, а целые индексы.
     * Получение элемента ArrayList по индексу обходится в О(1)
     * В пределе n раз приходится получать ветвь с затратами О(1) и просто целиком ее (т.к. это List размером m)
     * добавлять к итоговому списку с затратами О(m)
     * Итого O(n*m)
     */
    private ArrayList<ArrayList<List<Nest>>> arrayListMapping;

    public List<Nest> getNestsFromIndexedTree(Integer treeId, List<Integer> branchIds) {

        if (treeId == null || branchIds == null || branchIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Nest> result = new LinkedList<>();
        ArrayList<List<Nest>>  tree = arrayListMapping.get(treeId); //O(1)

        branchIds.stream().map((key) -> tree.get(key)).forEach((collection) -> result.addAll(collection));

        return result;
    }

}
