package cinema.model;

import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private UUID token;
    private Seat ticket;

    public Ticket(Seat ticket) {
        this.ticket = ticket;
        token = UUID.randomUUID();
    }

    public Ticket() {
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Seat getTicket() {
        return ticket;
    }

    public void setTicket(Seat ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return token.equals(ticket.token) && this.ticket.equals(ticket.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, ticket);
    }
}
