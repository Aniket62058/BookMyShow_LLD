package com.aniket.BookMyShow.service;

import com.aniket.BookMyShow.Exceptions.TicketNotFoundException;
import com.aniket.BookMyShow.Exceptions.UserAlreadyExistException;
import com.aniket.BookMyShow.Exceptions.UserNotFoundException;
import com.aniket.BookMyShow.models.*;
import com.aniket.BookMyShow.repository.ShowSheatRepository;
import com.aniket.BookMyShow.repository.TicketRepository;
import com.aniket.BookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService{
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ShowSheatRepository showSheatRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Ticket bookTicket(Long userId, List<Long> showSeatsIds, Long showId) {
        return null;
    }

    @Override
    public Ticket cancelTicket(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if(optionalTicket.isEmpty()){
            throw new TicketNotFoundException("Ticket for given id not found");
        }
        Ticket ticket= optionalTicket.get();
        ticket.setBookingStatus(BookingStatus.CANCELED);
        for(ShowSeat showSeat: ticket.getShowSeats()){
            showSeat.setShowSeatStatus(ShowSeatStatus.AVAILABLE);
            showSheatRepository.save(showSeat);
        }
        ticketRepository.save(ticket);
        for(Payment payment: ticket.getPayments()){
            int refNo = payment.getRefNo();
            // send a message to third party with payment ref number for refund
        }

        return ticket;
    }

    @Override
    public Ticket transferTicket(Long ticketId, Long fromUserId, Long toUserId) {
        Optional<User> fromUserOptional = userRepository.findById(fromUserId);
        Optional<User> toUserOptional = userRepository.findById(toUserId);
        if(fromUserOptional.isEmpty() || toUserOptional.isEmpty()){
            throw new UserNotFoundException("User details for given ticket transfer is not found");
        }
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if(optionalTicket.isEmpty()){
            throw new TicketNotFoundException("Ticket for given id not found");
        }

        Ticket ticket= optionalTicket.get();
        User fromUser = fromUserOptional.get();
        User toUser = toUserOptional.get();

        List<Ticket> bookedTicketHistory = fromUser.getTickets();
        bookedTicketHistory.remove(ticket);
        userRepository.save(fromUser);

        bookedTicketHistory = toUser.getTickets();
        bookedTicketHistory.add(ticket);
        toUser = userRepository.save(toUser);

        ticket.setUser(toUser);
        return ticketRepository.save(ticket);
    }
}
