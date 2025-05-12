package Project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.LinkedList;
import java.util.Scanner;

import static Project1.ConcertManager.recentTickets;


public class ConcertManagement {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ConcertManager manager = new ConcertManager();



        // Sample data
        Admin admin = new Admin(1, "AdminUser", "admin@example.com");
        Attendee sampleAttendee = new Attendee(2, "John Doe");

        manager.addUser(admin);
        manager.addUser(sampleAttendee);

        Venue venue1 = new Venue(1, "City Hall", "Downtown", 5000);
        Concert concert1 = new Concert(101, "Rock Night", "2025-06-01", venue1);
        Performer performer1 = new Performer(1, "The Rockers", "Rock");

        concert1.addPerformer(performer1);
        admin.addConcert(manager, concert1);

        boolean running = true;
        while (running) {
            System.out.println("\n--- Concert Management System ---");
            System.out.println("1. View Concerts");
            System.out.println("2. Search Concert by Title (Linear Search)");
            System.out.println("3. Search Concert by ID (Binary Search)");
            System.out.println("4. Book Ticket");
            System.out.println("5. View Users");
            System.out.println("6. Add a New Concert");
            System.out.println("7. Undo Ticket Booking");
            System.out.println("8. View Recently Booked Tickets");
            System.out.println("9. Search User by ID (BST)");


            System.out.println("0. Exit");

            System.out.print("Select an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    manager.displayAllConcerts();
                    break;

                case 2:
                    System.out.print("Enter concert title: ");
                    String title = sc.nextLine();
                    Concert searchTitle = manager.searchConcertByTitle(title);
                    if (searchTitle != null) {
                        searchTitle.displayConcert();
                    } else {
                        System.out.println("Concert not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter concert ID: ");
                    int id = sc.nextInt();
                    Concert searchId = manager.searchConcertById(id);
                    if (searchId != null) {
                        searchId.displayConcert();
                    } else {
                        System.out.println("Concert not found.");
                    }
                    break;

                case 4:
                    manager.displayAllConcerts();
                    System.out.print("Enter your user ID: ");
                    int userId = sc.nextInt();
                    sc.nextLine(); // Clear buffer

                    Attendee attendee;

                    if (!manager.getUsers().containsKey(userId)) {
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        attendee = new Attendee(userId, name);
                        manager.addUser(attendee);
                    } else {
                        User user = manager.getUsers().get(userId);

                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();

                        if (user instanceof Attendee) {
                            attendee = (Attendee) user;
                        } else {
                            System.out.println("❌ This user is not an attendee. Only attendees can book tickets.");
                            break;
                        }
                    }

                    System.out.print("Enter Concert ID to book ticket: ");
                    int concertIdToBook = sc.nextInt();
                    Concert concertToBook = manager.searchConcertById(concertIdToBook);
                    if (concertToBook != null) {
                        attendee.bookTicket(concertToBook, manager);

                        // ✅ Show the latest ticket booked
                        Ticket latestTicket = attendee.tickets.getLast();
                        System.out.println("\nYour Ticket:");
                        System.out.println(latestTicket);
                    } else {
                        System.out.println("Concert not found.");
                    }
                    break;

                case 5:
                    admin.viewAllUsers(manager);
                    break;

                case 6:
                    System.out.print("Enter Concert ID: ");
                    int newConcertId = sc.nextInt();
                    sc.nextLine(); // clear buffer

                    System.out.print("Enter Concert Title: ");
                    String newTitle = sc.nextLine();

                    System.out.print("Enter Concert Date (YYYY-MM-DD): ");
                    String newDate = sc.nextLine();

                    System.out.print("Enter Venue ID: ");
                    int venueId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Venue Name: ");
                    String venueName = sc.nextLine();

                    System.out.print("Enter Venue Location: ");
                    String venueLoc = sc.nextLine();

                    System.out.print("Enter Venue Capacity: ");
                    int capacity = sc.nextInt();
                    sc.nextLine();

                    Venue newVenue = new Venue(venueId, venueName, venueLoc, capacity);
                    Concert newConcert = new Concert(newConcertId, newTitle, newDate, newVenue);

                    System.out.print("Enter number of performers: ");
                    int performerCount = sc.nextInt();
                    sc.nextLine();

                    for (int i = 0; i < performerCount; i++) {
                        System.out.print("Enter Performer ID: ");
                        int pid = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Performer Name: ");
                        String pname = sc.nextLine();

                        System.out.print("Enter Performer Genre: ");
                        String pgenre = sc.nextLine();

                        Performer perf = new Performer(pid, pname, pgenre);
                        newConcert.addPerformer(perf);
                    }
                    admin.addConcert(manager, newConcert);
                    break;

                case 7:
                    System.out.print("Enter your user ID: ");
                    int uid = sc.nextInt();
                    if (manager.getUsers().containsKey(uid) && manager.getUsers().get(uid) instanceof Attendee) {
                        Attendee at = (Attendee) manager.getUsers().get(uid);
                        manager.getUsers().remove(uid);
                        at.undoLastBooking(manager);

                    } else {
                        System.out.println("Invalid Attendee ID.");
                    }
                    break;

                case 8:
                    manager.displayRecentBookings();
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                case 9:
                    System.out.print("Enter user ID to search: ");
                    int userIdSearch = sc.nextInt();
                    User foundUser = manager.searchUserById(userIdSearch);
                    if (foundUser != null) {
                        foundUser.displayInfo();
                    } else {
                        System.out.println("User not found.");
                    }
                    break;


                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }
}