package org.example;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        // Задание 1
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            System.out.println("Список покупателей:");
            for (Visitor visitor : visitors) {
                System.out.println(visitor.getName() + " " + visitor.getSurname());
            }

            System.out.println("Количество посетителей: " + visitors.length);
        } catch (IOException e){
            e.printStackTrace();
        }

        // Задание 2
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            Set<Book> uniqueBooks = new HashSet<>();

            for (Visitor visitor : visitors) {
                uniqueBooks.addAll(visitor.getFavoriteBooks());
            }

            System.out.println("Список уникальных книг:");
            for (Book book : uniqueBooks) {
                System.out.println(book.getName() + " - " + book.getAuthor());
            }

            System.out.println("Количество уникальных книг: " + uniqueBooks.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание 3
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            Set<Book> uniqueBooks = new HashSet<>();

            for (Visitor visitor : visitors) {
                uniqueBooks.addAll(visitor.getFavoriteBooks());
            }

            List<Book> bookList = new ArrayList<>(uniqueBooks);

            bookList.sort(Comparator.comparingInt(Book::getPublishingYear));

            System.out.println("Список книг, отсортированный по году издания:");
            for (Book book : bookList) {
                System.out.println(book.getName() + " - " + book.getAuthor() + " (" + book.getPublishingYear() + ")");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание 4
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            for (Visitor visitor : visitors) {
                for (Book book : visitor.getFavoriteBooks()) {
                    if ("Jane Austen".equals(book.getAuthor())) {
                        System.out.println(visitor.getName() + " " + visitor.getSurname() + " имеет в избранном книгу: " + book.getName());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание 5
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            int maxBooks = 0;
            Visitor maxVisitor = null;

            for (Visitor visitor : visitors) {
                int numberOfBooks = visitor.getFavoriteBooks().size(); // Количество книг у текущего посетителя

                if (numberOfBooks > maxBooks) {
                    maxBooks = numberOfBooks;
                    maxVisitor = visitor;
                }
            }

            if (maxVisitor != null) {
                System.out.println("Посетитель с максимальным количеством книг в избранном: "
                        + maxVisitor.getName() + " " + maxVisitor.getSurname()
                        + " с " + maxBooks + " книгами.");
            } else {
                System.out.println("Нет посетителей с книгами в избранном.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание 6
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            int totalBooks = 0;
            int subscribedVisitorsCount = 0;

            for (Visitor visitor : visitors) {
                if (visitor.isSubscribed()) {
                    totalBooks += visitor.getFavoriteBooks().size();
                    subscribedVisitorsCount++;
                }
            }

            double averageBooks = subscribedVisitorsCount > 0 ? (double) totalBooks / subscribedVisitorsCount : 0;

            // Создаем список SMS-сообщений
            List<SmsMessage> smsMessages = new ArrayList<>();

            // Группируем посетителей по категориям
            for (Visitor visitor : visitors) {
                if (visitor.isSubscribed()) {
                    int numberOfBooks = visitor.getFavoriteBooks().size();
                    String message;

                    if (numberOfBooks > averageBooks) {
                        message = "you are a bookworm";
                    } else if (numberOfBooks < averageBooks) {
                        message = "read more";
                    } else {
                        message = "fine";
                    }

                    // Добавляем SMS-сообщение в список
                    smsMessages.add(new SmsMessage(visitor.getPhone(), message));
                }
            }

            // Выводим SMS-сообщения на экран
            System.out.println("SMS-сообщения:");
            for (SmsMessage sms : smsMessages) {
                System.out.println("На номер: " + sms.getPhoneNumber() + " отправлено сообщение: " + sms.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

@Data
class Book {
    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;
}

@Data
class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;
}

@Data
@AllArgsConstructor
class SmsMessage {
    private String phoneNumber;
    private String message;

}