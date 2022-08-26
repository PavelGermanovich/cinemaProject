package cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Cinema {
    private int totalRows;
    private int totalColumns;
    private List<Seat> availableSeats;
    private List<Ticket> purchasedTicket;

    public Cinema() {
    }

    public Cinema(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        availableSeats = new ArrayList<>();
        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= totalColumns; col++) {
                int price = row <= 4 ? 10 : 8;
                availableSeats.add(new Seat(row, col, price));
            }
        }
        purchasedTicket = new ArrayList<>();
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats.stream().filter(seat -> !seat.isPurchased()).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Seat> getAllSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(List<Seat> availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }

    @JsonIgnore
    public List<Ticket> getPurchasedTicket() {
        return purchasedTicket;
    }

    public void addPurchasedTicket(Ticket ticket) {
        purchasedTicket.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        purchasedTicket.remove(ticket);
    }

    @JsonIgnore
    public int getPuchasedIncome() {
        return purchasedTicket.stream().mapToInt(ticket -> ticket.getTicket().getPrice()).sum();
    }

}
