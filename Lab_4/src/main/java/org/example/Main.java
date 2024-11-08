package org.example;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        // Задание 1
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            System.out.println("Список покупателей:");
            Arrays.stream(visitors)
                    .forEach(visitor -> System.out.println(visitor.getName() + " " + visitor.getSurname()));

            System.out.println("Количество посетителей: " + visitors.length);

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Задание 2
        Set<Book> uniqueBooks = new HashSet<>();
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            uniqueBooks = Arrays.stream(visitors)
                    .flatMap(e -> e.getFavoriteBooks().stream())
                    .collect(Collectors.toSet());

            System.out.println("Список уникальных книг:");
            uniqueBooks.forEach(book -> System.out.println(book.getName()));
            System.out.println("Количество уникальных книг: " + uniqueBooks.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание 3
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            uniqueBooks = Arrays.stream(visitors)
                    .flatMap(e -> e.getFavoriteBooks().stream())
                    .collect(Collectors.toSet());

            System.out.println("Список книг, отсортированный по году издания:");
            uniqueBooks.forEach(book -> System.out.println(book.getName() + " - " + book.getAuthor() + " (" + book.getPublishingYear() + ")"));

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Задание 4
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            uniqueBooks = Arrays.stream(visitors)
                    .flatMap(e -> e.getFavoriteBooks().stream())
                    .collect(Collectors.toSet());

            boolean hasAusten = uniqueBooks.stream()
                    .anyMatch(e -> e.getAuthor().equals("Jane Austen"));

            if (hasAusten) System.out.println("У одного из пользователей есть книга Jane Austen");
            else System.out.println("Ни у одного из пользователей нет книги Jane Austen");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание 5
        try (FileReader reader = new FileReader("src/main/resources/books.json")) {
            Visitor[] visitors = gson.fromJson(reader, Visitor[].class);

            Optional<Visitor> maxVisitor = Arrays.stream(visitors)
                    .max(Comparator.comparingInt(visitor -> visitor.getFavoriteBooks().size()));

            if (maxVisitor.isPresent()) {
                int maxBooks = maxVisitor.get().getFavoriteBooks().size();
                System.out.println("Максимальное количество книг = " + maxBooks);
                System.out.println("Посетитель с максимальным количеством книг в избранном: "
                        + maxVisitor.get().getName() + " " + maxVisitor.get().getSurname()
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

            double averageBooks = Arrays.stream(visitors)
                    .filter(Visitor::isSubscribed)
                    .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                    .average()
                    .orElse(0);

            List<SmsMessage> smsMessages = Arrays.stream(visitors)
                    .filter(Visitor::isSubscribed)
                    .map(visitor -> {
                        int numberOfBooks = visitor.getFavoriteBooks().size();
                        String message;

                        if (numberOfBooks > averageBooks) {
                            message = "you are a bookworm";
                        } else if (numberOfBooks < averageBooks) {
                            message = "read more";
                        } else {
                            message = "fine";
                        }

                        return new SmsMessage(visitor.getPhone(), message);
                    })
                    .toList();

            System.out.println("SMS-сообщения:");
            smsMessages.forEach(sms ->
                    System.out.println("На номер: " + sms.getPhoneNumber() + " отправлено сообщение: " + sms.getMessage()));

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