$(document).ready(function () {
    loadAllBookings();

    function loadAllBookings() {
        $.ajax({
            url: "http://localhost:8080/api/v1/booking/getAll",
            type: "GET",
            success: function (response) {
                console.log("Bookings:", response.data);

                // Clear the table body first
                $("#bookingTableBody").empty();

                // Loop through and append each booking to the table
                response.data.forEach(booking => {
                    let row = `
                    <tr>
                        <td>${booking.packageName}</td>
                        <td>${booking.guideEmail}</td>
                        <td>${booking.vehicleNumber}</td>
                        <td>${booking.userEmail}</td>
                        <td>${booking.bookingDate}</td>
                        <td>${booking.checkoutDate}</td>
                        <td>${booking.estimatedDays}</td>
                        <td>${booking.numberOfPeople}</td>
                        <td>LKR ${booking.guideFee.toFixed(2)}</td>
                        <td>LKR ${booking.basePrice.toFixed(2)}</td>
                        <td>LKR ${booking.totalPrice.toFixed(2)}</td>
                         <td>
                            <button class="btn btn-sm btn-primary" onclick="editBooking('${booking.id}')">Edit</button>
                            <button class="btn btn-sm btn-danger" onclick="deleteBooking('${booking.id}')">Delete</button>
                            <button class="btn btn-sm btn-info" onclick="viewBooking('${booking.id}')">View</button>
                        </td>
                    </tr>
                `;
                    $("#bookingTableBody").append(row);
                });
            },
            error: function (xhr) {
                console.error("Failed to fetch bookings:", xhr.responseText);
                alert("Failed to load bookings.");
            }
        });
    }
    $("#searchBooking").on("keyup", function () {
        const searchValue = $(this).val().toLowerCase();

        $("#bookingTableBody tr").filter(function () {
            const rowText = $(this).text().toLowerCase();
            $(this).toggle(rowText.includes(searchValue));
        });
    });
});


