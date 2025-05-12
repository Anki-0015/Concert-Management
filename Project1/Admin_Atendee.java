package Project1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
// Admin class
class Admin extends User {
    // Constructor to initialize Admin object with userId, name, and email
    public Admin(int userId, String name, String email) {
        super(userId, name, email);
    }
    // Adds a new concert to the ConcertManager if the concert ID is not already used
    public void addConcert(ConcertManager manager, Concert concert) {
        if (manager.searchConcertById(concert.getConcertId()) != null) {
            System.out.println("❌ Error: Concert with ID " + concert.getConcertId() + " already exists.");
            return;
        }
        manager.addConcert(concert);
        System.out.println("Concert added successfully.");
    }

    // Displays the Admin's basic information
    @Override
    public void displayInfo() {
        System.out.println("Admin: " + name + " (ID: " + userId + ")");
    }

    // Displays a list of all attendees along with their booked concert details
    public void viewAllUsers(ConcertManager manager) {
        System.out.println("\n--- User List ---");
        System.out.printf("%-10s %-20s %-15s %-20s %-10s\n", "User ID", "Name", "Concert ID", "Concert Title", "Price");

        // Iterate over all users
        for (User user : manager.getUsers().values()) {
            if (user instanceof Attendee) {
                Attendee attendee = (Attendee) user;
                for (Ticket ticket : attendee.tickets) {
                    // Print the user and ticket information in a formatted manner
                    System.out.printf("%-10d %-20s %-15d %-20s $%-10.2f\n",
                            attendee.userId,
                            attendee.getName(),
                            ticket.getConcert().getConcertId(),
                            ticket.getConcert().getTitle(),
                            ticket.getPrice());
                }
            }
        }
    }
}

// Attendee class


class Attendee extends User {
    LinkedList<Ticket> tickets = new LinkedList<>();
    Stack<Ticket> ticketHistory = new Stack<>(); // For undo feature

    // Constructor that sets userId, name, and auto-generates email from name
    public Attendee(int userId, String name) {
        super(userId, name, name.toLowerCase().replace(" ", "") + "@example.com");
    }

    // Books a ticket for a concert if not already booked, saves to file, and updates relevant structures
    public void bookTicket(Concert concert,ConcertManager manager) {
        for (Ticket ticket : tickets) {
            if (ticket.getConcert().getConcertId() == concert.getConcertId()) {
                System.out.println("❌ You have already booked a ticket for this concert.");
                return; // Prevent booking the same concert again
            }
        }
        Ticket ticket = new Ticket(concert, tickets.size() + 1, 100.0, userId, name);
        tickets.add(ticket);
        ticketHistory.push(ticket); // Track for undo
        manager.getUsers().put(userId, this);
        manager.userTree.insert(this);
        ConcertManager.recentTickets.add(ticket); // Add to stack

        try (FileWriter fw = new FileWriter("tickets.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(ticket.toFileString());
        } catch (IOException e) {
            System.out.println("Error saving ticket: " + e.getMessage());
        }
        System.out.println("Ticket booked for concert: " + concert.getTitle());
    }

    // Undoes the last booked ticket and removes it from all data structures
    public void undoLastBooking(ConcertManager manager) {
        if (!ticketHistory.isEmpty()) {
            Ticket last = ticketHistory.pop();
            tickets.remove(last);
            manager.users.remove(last.getTicketId());
            manager.userTree.remove(this.userId);
            manager.recentTickets.remove(last);
            System.out.println("Last booking undone: " + last.getConcert().getTitle());
        } else {
            System.out.println("No bookings to undo.");
        }
    }

    // Displays attendee's information and all booked tickets
    @Override
    public void displayInfo() {
        System.out.println("Attendee: " + name + " (ID: " + userId + ")");
        if (tickets.isEmpty()) {
            System.out.println("No tickets booked yet.");
        } else {
            System.out.println("Tickets booked:");
            for (Ticket ticket : tickets) {
                System.out.println("Ticket ID: " + ticket.getTicketId() + ", Concert: " + ticket.getConcert().getTitle() + ", Price: $" + ticket.getPrice());
            }
        }
    }

//    public void addTicket(Ticket ticket) {
//        tickets.add(ticket);
//    }

    // Returns the name of the attendee
    public String getName() {
        return name;
    }
}
