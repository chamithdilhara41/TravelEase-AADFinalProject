$(document).ready(function () {
    // Fetch and display all guides
    function fetchAllGuides() {
        $.ajax({
            url: "http://localhost:8080/api/v1/guide/getAll",
            type: "GET",
            success: function (response) {
                if (response.code === 201) {
                    let guides = response.data;
                    let guideHTML = "";
                    guides.forEach(guide => {
                        guideHTML += `
                                    <tr>
                                        <td>${guide.fullName}</td>
                                        <td>${guide.email}</td>
                                        <td><img class="img-fluid"  style="width: 70px; height: 70px;" src="uploads/${guide.imageUrl}" alt="Guide Image"></td>
                                        <td>${guide.description}</td>
                                        <td>${guide.paymentPerDay}</td>
                                        <td><a href="${guide.linkedin}" target="_blank">LinkedIn</a></td>
                                        <td>${guide.booked}</td>
                                        <td>${guide.status}</td>
                                        <td>
                                             <button class="btn btn-warning btn-sm edit-guide-btn"
                                                data-name="${guide.fullName}"
                                                data-email="${guide.email}"
                                                data-description="${guide.description}"
                                                data-payment="${guide.paymentPerDay}"
                                                data-linkedin="${guide.linkedin}"
                                                data-image="uploads/${guide.imageUrl}"
                                                data-bs-toggle="modal"
                                                data-bs-target="#editGuideModal">
                                                Edit
                                            </button>
                                            <button class="btn btn-success btn-sm activate-guide-btn" data-email="${guide.email}">Activate</button><br>
                                            <button class="btn btn-danger btn-sm deactivate-guide-btn" data-email="${guide.email}">Deactivate</button>
                                        </td>
                                    </tr>
                                `;
                    });
                    $("#guideTableBody").html(guideHTML);
                } else {
                    $("#guideTableBody").html("<tr><td colspan='4'>No guides found.</td></tr>");
                }
            },
            error: function () {
                $("#guideTableBody").html("<tr><td colspan='4'>Error loading guides.</td></tr>");
            }
        });
    }

    fetchAllGuides();

    $("#addGuideForm").submit(function (event) {
        event.preventDefault();
        let formData = new FormData();
        formData.append("fullName", $("#guideName").val());
        formData.append("email", $("#guideEmail").val());
        formData.append("description", $("#guideDescription").val());
        formData.append("paymentPerDay", $("#guidePayment").val());
        formData.append("linkedin", $("#guideLinkedIn").val());
        formData.append("imageUrl", $("#guideImage")[0].files[0]);

        $.ajax({
            url: "http://localhost:8080/api/v1/guide/save",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("Guide Added Successfully!");
                $("#addGuideModal").modal("hide");
                $("#addGuideForm")[0].reset();
                $("#imagePreview").hide();
                fetchAllGuides();
                $("#editGuideModal").modal("hide");
                // after and go to mail for the guide your registration is success, how to do that plz gide me
            },
            error: function () {
                alert("Error adding guide.");
            }
        });
    });

// ✅ Live Search Functionality
    $("#searchGuide").on("keyup", function () {
        let value = $(this).val().toLowerCase();
        $("#guideTableBody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });

// ✅ Image Preview on File Select
    $("#guideImage").on("change", function (event) {
        let reader = new FileReader();
        reader.onload = function (e) {
            $("#imagePreview").attr("src", e.target.result).show();
        };
        reader.readAsDataURL(event.target.files[0]);
    });

    $(document).on("click", ".edit-guide-btn", function () {
        // Get guide details from button attributes
        let guideId = $(this).data("id");
        let guideName = $(this).data("name");
        let guideEmail = $(this).data("email");
        let guideDescription = $(this).data("description");
        let guidePayment = $(this).data("payment");
        let guideLinkedIn = $(this).data("linkedin");
        let guideImage = $(this).data("image");

        // Fill modal fields
        $("#editGuideId").val(guideId);
        $("#editGuideName").val(guideName);
        $("#editGuideEmail").val(guideEmail);
        $("#editGuideDescription").val(guideDescription);
        $("#editGuidePayment").val(guidePayment);
        $("#editGuideLinkedIn").val(guideLinkedIn);

        // Show existing image if available
        if (guideImage) {
            $("#editImagePreview").attr("src", guideImage).show();
        } else {
            $("#editImagePreview").hide();
        }

        // Image preview when selecting a new image
        $("#editGuideImage").on("change", function (event) {
            let file = event.target.files[0];
            if (file) {
                let reader = new FileReader();
                reader.onload = function (e) {
                    $("#editImagePreview").attr("src", e.target.result).show();
                };
                reader.readAsDataURL(file);
            }
        });
    });

    // Handle Update Form Submission
    $("#editGuideForm").on("submit", function (e) {
        e.preventDefault();

        let formData = new FormData(this);
        let email = $("#editGuideEmail").val();

        $.ajax({
            url: `http://localhost:8080/api/v1/guide/update/${email}`, // Your API endpoint
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Guide updated successfully!");
                $("#editGuideModal").modal("hide");
                fetchAllGuides();
            },
            error: function () {
                alert("Error updating guide.");
            }
        });
    });

    $(document).on("click", ".activate-guide-btn, .deactivate-guide-btn", function () {

        let email = $(this).data("email");
        let action = $(this).hasClass("activate-guide-btn") ? "activate" : "deactivate";
        console.log(action)
        let url = `http://localhost:8080/api/v1/guide/${action}/${email}`; // Constructing the URL dynamically
        let method = "PUT"; // We're making a PUT request

        $.ajax({
            url: url, // The dynamic URL for the action
            type: method, // Use PUT method
            contentType: "application/json",
            success: function (response) {
                alert(response.message); // Display success message
                fetchAllGuides()
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
                alert("Something went wrong!");
            }
        });
    });

});
