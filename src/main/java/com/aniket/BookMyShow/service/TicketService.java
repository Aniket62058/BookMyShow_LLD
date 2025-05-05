package com.aniket.BookMyShow.service;

import com.aniket.BookMyShow.models.Ticket;

import java.util.List;

public interface TicketService {
    Ticket bookTicket(Long userId, List<Long> showSeatsIds, Long showId);
    Ticket cancelTicket(Long ticketId);
    Ticket transferTicket(Long ticketId, Long fromUserId, Long toUserId);
}
