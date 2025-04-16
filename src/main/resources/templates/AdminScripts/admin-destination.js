$(document).ready(function () {
    // Image Preview
    $("#destinationImage").change(function () {
        let file = this.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                $("#imgPreview").attr("src", e.target.result).show();
            };
            reader.readAsDataURL(file);
        }
    });

    // Submit Form with Image and Category
    $("#addDestinationForm").submit(function (event) {
        event.preventDefault(); // Prevent form submission

        let formData = new FormData();
        formData.append("name", $("#destinationName").val());
        formData.append("description", $("#destinationDescription").val());
        formData.append("location", $("#destinationLocation").val());
        formData.append("costPerDay", $("#destinationCost").val());
        formData.append("category", $("#destinationCategory").val());
        formData.append("imageUrl", $("#destinationImage")[0].files[0]); // Append Image File

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/v1/destination/save", // Adjust URL based on your server
            processData: false,
            contentType: false,
            data: formData,
            success: function (response) {
                alert("Destination Saved Successfully!");
                $("#addDestinationModal").modal("hide");
                $("#addDestinationForm")[0].reset();
                $("#imgPreview").hide();
                loadDestinations(); // Refresh the table with new data
            },
            error: function (error) {
                alert("Error saving destination!");
                console.error(error);
            }
        });
    });

    // Load all destinations
    function loadDestinations() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/v1/destination/getAll", // Adjust URL based on your server
            success: function (destinations) {
                $("#destinationTableBody").empty();
                destinations.data.forEach(function (destination) {
                    $("#destinationTableBody").append(`
                        <tr>
                            <td>${destination.id}</td>
                            <td>${destination.name}</td>
                            <td>${destination.description}</td>
                            <td>${destination.location}</td>
                            <td>${destination.category}</td>
                            <td>${destination.costPerDay}</td>
                            <td><img src="uploads/${destination.imageUrl}" width="100"></td>
                            <td>
                                <button class="btn btn-warning btn-sm edit-destination-btn" 
                                    data-id="${destination.id}"
                                    data-name="${destination.name}"
                                    data-description="${destination.description}"
                                    data-location="${destination.location}"
                                    data-category="${destination.category}"
                                    data-costperday="${destination.costPerDay}"
                                    data-imageurl="uploads/${destination.imageUrl}"
                                    data-bs-toggle="modal"
                                    data-bs-target="#editDestinationModal">
                                    Edit
                                </button>
                                <button class="btn btn-danger btn-sm delete-destination-btn" data-id="${destination.id}">Delete</button>
                            </td>
                        </tr>
                    `);
                });
            },
            error: function (error) {
                console.error("Error fetching destinations:", error);
            }
        });


    }

    loadDestinations();


    $(document).on("click", ".edit-destination-btn", function () {
        // Get destination details from button attributes
        let destinationId = $(this).data("id");
        let destinationName = $(this).data("name");
        let destinationDescription = $(this).data("description");
        let destinationLocation = $(this).data("location");
        let destinationCategory = $(this).data("category");
        let destinationCost = $(this).data("costperday");
        let destinationImage = $(this).data("imageurl");

        $("#editDestinationId").val(destinationId);
        $("#editDestinationName").val(destinationName);
        $("#editDestinationDescription").val(destinationDescription);
        $("#editDestinationLocation").val(destinationLocation);
        $("#editDestinationCategory").val(destinationCategory);
        $("#editDestinationCostPerDay").val(destinationCost);

        // Show existing image if available
        if (destinationImage) {
            $("#editImgPreview").attr("src", destinationImage).show();
        } else {
            $("#editImgPreview").hide();
        }

        // Remove old event listener to prevent multiple bindings
        $("#editDestinationImage").off("change").on("change", function (event) {
            let file = event.target.files[0];
            if (file) {
                let reader = new FileReader();
                reader.onload = function (e) {
                    $("#editImgPreview").attr("src", e.target.result).show();
                };
                reader.readAsDataURL(file);
            }
        });

        // Show the modal
        $("#editDestinationModal").modal("show");

        $("#editDestinationForm").on("submit", function (e) {
            e.preventDefault();

            let formData = new FormData(this);
            let id = $("#editDestinationId").val(); // Ensure this field exists in the modal

            if (!id || id === "undefined") {
                alert("Error: Destination ID is missing.");
                return;
            }

            $.ajax({
                url: `http://localhost:8080/api/v1/destination/update/${id}`, // Your API endpoint
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function () {
                    alert("Destination updated successfully!");
                    $("#editDestinationModal").modal("hide");
                    loadDestinations();
                },
                error: function () {
                    alert("Error updating destination.");
                }
            });
        });
    });
    // Delete Destination functionality
    $(document).on("click", ".delete-destination-btn", function () {
        let id = $(this).data("id");
        console.log(id);

        if (confirm("Are you sure you want to delete this destination?")) {
            $.ajax({
                url: `http://localhost:8080/api/v1/destination/delete/${id}`,
                type: "DELETE",
                success: function () {
                    alert("Destination deleted successfully!");
                    loadDestinations();
                },
                error: function () {
                    alert("Error deleting destination.");
                }
            });
        }
    });

});
