//package JA_home6;

import java.util.*;
import java.util.Scanner;

public class Operation {
    private Set<Notebook> notebooks = new HashSet<>();
    private List<Criterion> criterionList = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public void printList() {
        for (Notebook notebook : notebooks) {
            if (notebookIsCorrect(notebook)) {
                System.out.println(notebook);
            }
        }
    }

    public boolean notebookIsCorrect(Notebook notebook) {
        for (Criterion criterion : criterionList) {
            Object valueNotebook = null;

            if (criterion.property.equals("name")) {
                valueNotebook = notebook.getId();
            } else if (criterion.property.equals("RAMamount")) {
                valueNotebook = notebook.getRam();
            } else if (criterion.property.equals("operatingSystem")) {
                valueNotebook = notebook.getOperatingSystem();
            } else if (criterion.property.equals("hard")) {
                valueNotebook = notebook.getHard();
            } else if (criterion.property.equals("model")) {
                valueNotebook = notebook.getModel();
            } else {
                continue;
            }

            if (criterion.value != null && !criterion.value.equals(valueNotebook)) {
                return false;
            }

            if (criterion.maxValue != null
                    && criterion.maxValue < Double.parseDouble(Objects.toString(valueNotebook))) {
                return false;
            }

            if (criterion.minValue != null
                    && criterion.minValue > Double.parseDouble(Objects.toString(valueNotebook))) {
                return false;
            }
        }
        return true;
    }

    public Operation(Set<Notebook> notebooks) {
        this.sc = new Scanner(System.in);
        this.notebooks = notebooks;
    }

    public Operation(Set<Notebook> notebooks, List<Criterion> criterionList) {
        this.sc = new Scanner(System.in);
        this.notebooks = notebooks;
        this.criterionList = criterionList;
    }

    public int getCriteria() {
        String text = "Введите цифру, соответствующую необходимому критерию! ";
        List<String> properties = filterProperties();

        for (int i = 0; i < properties.size(); i++) {
            text += "\n" + (i + 1) + ". " + getPropertyDescription(properties.get(i));
        }
        System.out.println(text);
        System.out.print("необходимый критерий -> ");
        int value = sc.nextInt();
        return value;
    }

    public String getPropertyDescription(String property) {
        Map<String, String> descriptionsProperties = descriptionsProperties();
        return descriptionsProperties.get(property);
    }

    public Map<String, String> descriptionsProperties() {
        Map<String, String> map = new HashMap<>();

        map.put("name", "По Id");
        map.put("RAMamount", "Объем оперативной памяти");
        map.put("operatingSystem", "Операционная система");
        map.put("hard", "Объем ЖД");
        map.put("model", "Модель");
        return map;
    }

    public List<String> filterProperties() {
        List<String> list = new ArrayList<>();
        list.add("name");
        list.add("RAMamount");
        list.add("operatingSystem");
        list.add("hard");
        list.add("model");
        return list;
    }

    public String getOperations() {
        String text = "   --------------\n        Меню \n   --------------\n " +
                "1. Выбор параметра \n " +
                "2. Список ноутбуков\n " +
                "3. Выход";
        System.out.println(text);
        System.out.print(" Сделайте выбор -> ");
        String answer = sc.next();
        return answer;
    }

    public Set<String> quantitativeSelection() {
        Set<String> set = new HashSet<>();
        set.add("RAMamount");
        set.add("hard");
        return set;
    }

    public Set<String> stringSelection() {
        Set<String> set = new HashSet<>();
        set.add("name");
        set.add("operatingSystem");
        set.add("model");
        return set;
    }

    public void start() {
        boolean flag = true;
        while (flag) {
            String operation = getOperations();
            if (operation.equals("3")) {
                flag = false;
                sc.close();
                continue;
            } else if (operation.equals("1")) {

                int criterion = getCriteria();
                List<String> properties = filterProperties();
                if (criterion - 1 < 0 || criterion - 1 > properties.size() - 1) {
                    System.out.println("Введено некорректное значение");
                    continue;
                }
                String property = properties.get(criterion - 1);
                Criterion criterionObject = null;
                try {
                    if (quantitativeSelection().contains(property)) {
                        criterionObject = Criterion.startGetting(sc, property, true);
                    } else {
                        criterionObject = Criterion.startGetting(sc, property, false);
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при выборе");
                    continue;
                }
                if (criterionObject != null) {
                    System.out.println("Ваш критерий добавлен!");
                    criterionList.add(criterionObject);
                    System.out.println("Промежуточный итог: ");
                    printList();
                }
            } else if (operation.equals("2")) {
                printList();
            }
        }
    }
}

class Criterion {

    Object value;
    Double minValue;
    Double maxValue;
    boolean isQuantitative;
    String property;

    public Criterion(String property, boolean isQuantitative, Object value, Double minValue, Double maxValue) {
        this.property = property;
        this.isQuantitative = isQuantitative;
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static Criterion startGetting(Scanner sc, String property, boolean isQuantitative) {
        if (isQuantitative) {
            String quest = "Выберите тип критерия: " +
                    "\n 1. По значению" +
                    "\n 2. Меньше заданного" +
                    "\n 3. Больше заданного" +
                    "\n 4. Интервал";
            System.out.println(quest);
            System.out.print("Выбор -> ");
            String text = sc.next();
            Criterion criterion = null;
            if (text.equals("1")) {
                System.out.print("Введите значение -> ");
                int getValue = sc.nextInt();
                criterion = new Criterion(property, isQuantitative, getValue, null, null);
            } else if (text.equals("2")) {
                System.out.print("Введите максимальное предельное значение -> ");
                double getValue = sc.nextDouble();
                criterion = new Criterion(property, isQuantitative, null, null, getValue);
            } else if (text.equals("3")) {
                System.out.print("Введите минимальное предельное значение -> ");
                double getValue = sc.nextDouble();
                criterion = new Criterion(property, isQuantitative, null, getValue, null);
            } else if (text.equals("4")) {
                System.out.print("Введите минимальное предельное значение -> ");
                double getMin = sc.nextDouble();
                System.out.print("Введите максимальное предельное значение -> ");
                double getMax = sc.nextDouble();
                criterion = new Criterion(property, isQuantitative, null, getMin, getMax);
            }

            return criterion;
        }

        System.out.print("Введите значение -> ");
        String getValue = sc.next();
        return new Criterion(property, isQuantitative, getValue, null, null);
    }

}