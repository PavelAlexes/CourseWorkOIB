import java.util.*;

// Класс для представления модели Харрисона-Руззо-Ульмана
public class HRUModel {
    private Map<String, Map<String, Set<String>>> accessMatrix; // Матрица доступа
    private static final Set<String> VALID_RIGHTS = Set.of("read", "write", "execute", "Read", "Write", "Execute"); // Допустимые права доступа


    public HRUModel() {
        accessMatrix = new HashMap<>();
    }

    // Добавление субъекта (пользователя или процесса)
    public void addSubject(String subject) {
        Map<String, Set<String>> objectMap = new HashMap<>();
        for (String object : getAllObjects()) {
            objectMap.put(object, new HashSet<>());
        }
        accessMatrix.putIfAbsent(subject, objectMap);
        System.out.println("Субъект " + subject + " добавлен.");

    }

    // Добавление объекта (файла или ресурса)
    public void addObject(String object) {
        for (Map<String, Set<String>> subjectMap : accessMatrix.values()) {
            subjectMap.putIfAbsent(object, new HashSet<>());
        }
        System.out.println("Объект " + object + " добавлен.");

    }

    // Назначение права доступа
    public void grantAccess(String subject, String object, String right) {
        if (!VALID_RIGHTS.contains(right)) {
            System.out.println("Ошибка: Некорректное право доступа. Допустимые права: read, write, execute.");
            return;
        }
        if (!accessMatrix.containsKey(subject)) {
            System.out.println("Предупреждение: Субъект " + subject + " не существует.");
            return;
        }
        if (!accessMatrix.get(subject).containsKey(object)) {
            System.out.println("Предупреждение: Объект " + object + " не существует.");
            return;
        }
        accessMatrix.get(subject).get(object).add(right);
        System.out.println("Доступ " + right + " предоставлен субъекту " + subject + " к объекту " + object);

    }


    public void revokeAccess(String subject, String object, String right) {
        if (!accessMatrix.containsKey(subject)) {
            System.out.println("Предупреждение: Субъект " + subject + " не существует.");
            return;
        }
        if (!accessMatrix.get(subject).containsKey(object)) {
            System.out.println("Предупреждение: Объект " + object + " не существует.");
            return;
        }
        if (accessMatrix.get(subject).get(object).remove(right)) {
            System.out.println("Доступ " + right + " отозван у субъекта " + subject + " к объекту " + object);
        } else {
            System.out.println("Предупреждение: Право " + right + " не найдено для субъекта " + subject + " на объекте " + object);
        }

    }

    // Проверка прав доступа
    public boolean checkAccess(String subject, String object, String right) {
        if (!VALID_RIGHTS.contains(right)) {
            System.out.println("Ошибка: Некорректное право доступа. Допустимые права: read, write, execute.");
            return false;
        }
        if (!accessMatrix.containsKey(subject)) {
            System.out.println("Предупреждение: Субъект " + subject + " не существует.");
            return false;
        }
        if (!accessMatrix.get(subject).containsKey(object)) {
            System.out.println("Предупреждение: Объект " + object + " не существует.");
            return false;
        }
        return accessMatrix.get(subject).get(object).contains(right);
    }

    // Получение всех объектов в системе
    private Set<String> getAllObjects() {
        Set<String> objects = new HashSet<>();
        for (Map<String, Set<String>> subjectMap : accessMatrix.values()) {
            objects.addAll(subjectMap.keySet());
        }
        return objects;
    }

    // Вывод текущего состояния матрицы доступа
    public void printAccessMatrix() {
        System.out.println("\nМатрица доступа:");
        for (String subject : accessMatrix.keySet()) {
            System.out.println("Субъект: " + subject);
            for (String object : accessMatrix.get(subject).keySet()) {
                System.out.println("  Объект: " + object + " | Права: " + accessMatrix.get(subject).get(object));
            }
        }
    }

    public static void main(String[] args) {
        HRUModel model = new HRUModel();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Добавить субъекта");
            System.out.println("2. Добавить объект");
            System.out.println("3. Назначить право доступа");
            System.out.println("4. Отозвать право доступа");
            System.out.println("5. Проверить доступ");
            System.out.println("6. Показать матрицу доступа");
            System.out.println("7. Выйти");
            System.out.print("Введите номер действия: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Очистка буфера

            switch (choice) {
                case 1:
                    System.out.print("Введите имя субъекта: ");
                    String subject = scanner.nextLine();
                    model.addSubject(subject);
                    break;
                case 2:
                    System.out.print("Введите имя объекта: ");
                    String object = scanner.nextLine();
                    model.addObject(object);
                    break;
                case 3:
                    System.out.print("Введите имя субъекта: ");
                    subject = scanner.nextLine();
                    System.out.print("Введите имя объекта: ");
                    object = scanner.nextLine();
                    System.out.print("Введите право доступа (например, read): ");
                    String right = scanner.nextLine();
                    model.grantAccess(subject, object, right);
                    break;
                case 4:
                    System.out.print("Введите имя субъекта: ");
                    subject = scanner.nextLine();
                    System.out.print("Введите имя объекта: ");
                    object = scanner.nextLine();
                    System.out.print("Введите право доступа для отзыва: ");
                    right = scanner.nextLine();
                    model.revokeAccess(subject, object, right);
                    break;
                case 5:
                    System.out.print("Введите имя субъекта: ");
                    subject = scanner.nextLine();
                    System.out.print("Введите имя объекта: ");
                    object = scanner.nextLine();
                    System.out.print("Введите право доступа: ");
                    right = scanner.nextLine();
                    boolean hasAccess = model.checkAccess(subject, object, right);
                    System.out.println("Доступ " + right + " у субъекта " + subject + " к объекту " + object + ": " + (hasAccess ? "Есть" : "Нет"));
                    break;
                case 6:
                    model.printAccessMatrix();
                    break;
                case 7:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор. Повторите попытку.");
            }
        }
    }
}