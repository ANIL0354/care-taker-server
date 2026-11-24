package com.careride.booking.service;

import com.careride.booking.dto.BookingRequest;
import com.careride.booking.dto.BookingResponse;

public interface BookingService {

    BookingResponse book(String caretakerEmail, BookingRequest request);
}
