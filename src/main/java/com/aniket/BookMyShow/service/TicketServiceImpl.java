package com.aniket.BookMyShow.service;

import com.aniket.BookMyShow.Exceptions.*;
import com.aniket.BookMyShow.models.*;
import com.aniket.BookMyShow.repository.ShowRepository;
import com.aniket.BookMyShow.repository.ShowSheatRepository;
import com.aniket.BookMyShow.repository.TicketRepository;
import com.aniket.BookMyShow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private ShowRepository showRepository;
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ticket bookTicket(Long userId, List<Long> showSeatsIds, Long showId) {
        Optional<User> bookedByUserOptional = userRepository.findById(userId);
        Optional<Show> showOptional = showRepository.findById(showId);
        if(bookedByUserOptional.isEmpty()){
            throw new UserNotFoundException("User with given userId does not exist : " + userId);
        }
        if(showOptional.isEmpty()){
            throw new ShowNotFoundException("Show with given showId does not exist : " + showId);
        }
        User bookedByUser = bookedByUserOptional.get();
        Show show = showOptional.get();

        for(Long showSeatId: showSeatsIds){
            ShowSeat showSeat = showSheatRepository.findById(showSeatId).get();
            if(showSeat.getShowSeatStatus().equals(ShowSeatStatus.AVAILABLE)){
                showSeat.setShowSeatStatus(ShowSeatStatus.LOCKED);
            } else {
                throw new ShowSeatNotAvailableException("Show seat is not available");
            }
            showSheatRepository.save(showSeat);
        }

        // TODO : Remove this update -> Locked to Booked into a different method
        boolean paymentDone = paymentCheck();
        List<ShowSeat> showSeats = new ArrayList<>();
        double amount = 0;
        if(paymentDone){
            for(Long showSeatId: showSeatsIds){
                ShowSeat showSeat = showSheatRepository.findById(showSeatId).get();
                showSeat.setShowSeatStatus(ShowSeatStatus.BOOKED);
                showSeat = showSheatRepository.save(showSeat);
                showSeats.add(showSeat);
                amount += showSeat.getPrice();
            }
        }
        return ticketGenerator(bookedByUser, show, showSeats, amount);
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

    private boolean paymentCheck(){
        return true;
    }

    private Ticket ticketGenerator(User user, Show show, List<ShowSeat> showSeats, double amount){
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setBookingStatus(BookingStatus.CONFIRMED);
        ticket.setShowSeats(showSeats);
        ticket.setAmount(amount);
        ticket.setBookedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}
