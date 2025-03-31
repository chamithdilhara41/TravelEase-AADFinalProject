$(document).ready(function () {
    fetchVehicles(); // Call function when the page loads

    function fetchVehicles() {
        $.ajax({
            url: `http://localhost:8080/api/v1/vehicle/getAll`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.log("API Response:", data.data); // Debugging step

                let tableBody = $("#vehicleTableBody");
                tableBody.empty(); // Clear previous data

                data.data.forEach(vehicle => {
                    let row = `
                <tr>
                    <td>${vehicle.vehicleNumber}</td>
                    <td>${vehicle.vehicleType}</td>
                    <td>${vehicle.brand}</td>
                    <td>${vehicle.model}</td>
                    <td>${vehicle.year}</td>
                    <td>${vehicle.color}</td>
                    <td>${vehicle.seatingCapacity}</td>
                    <td>${vehicle.fuelType}</td>
                    <td><img src="uploads/${vehicle.insuranceDocument}" alt="Insurance Document" width="50" height="50"></td>
                    <td><img src="uploads/${vehicle.registrationDocument}" alt="Registration Document" width="50" height="50"></td>
                    <td>
                        ${Array.isArray(vehicle.vehicleImages)
                            ? (function() {
                                let imagesHtml = '';
                                for (let i = 0; i < vehicle.vehicleImages.length; i++) {
                                    imagesHtml += `<img src="uploads/${vehicle.vehicleImages[i]}" width="50" height="50"> `;
                                }
                                return imagesHtml;
                            })()
                            : `<img src="${vehicle.vehicleImages}" width="50" height="50">`}
                    </td>
                    <td>${vehicle.status}</td>
                    <td>
                        <button class="btn btn-warning btn-sm">Edit</button>
                        <button class="btn btn-danger btn-sm">Delete</button>
                    </td>
                </tr>
                `;
                    tableBody.append(row);
                });
            },
            error: function (xhr, status, error) {
                console.error("Error fetching vehicles:", xhr.responseText);
            }
        });

    }
});
