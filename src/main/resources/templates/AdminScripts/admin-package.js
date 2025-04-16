function loadDestinations() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/destination/getAll',  // Replace with your backend API endpoint
        type: 'GET',
        success: function(data) {
            const $destinationSelect = $('#packageDestinations');
            $destinationSelect.empty();  // Clear existing options

            data.data.forEach(function(destination) {
                $destinationSelect.append(new Option(destination.name, destination.id));
            });
        },
        error: function(error) {
            console.error('Error loading destinations:', error);
        }
    });
}

// Call the function to load destinations when the modal is opened
$('#addPackageModal').on('shown.bs.modal', function () {
    loadDestinations();
});

// Handle form submission to save the package
$('#addPackageForm').on('submit', function(e) {
    e.preventDefault(); // Prevent default form submission

    const formData = new FormData(this); // Create FormData object from the form
    const selectedDestinations = $('#packageDestinations').val(); // ['1', '2', ...]

    // Clear any existing 'destinations' values from the formData
    formData.delete('destinations');

    // Append each destination ID separately (important for Spring Boot)
    selectedDestinations.forEach(destId => {
        formData.append('destinations', destId);
    });

    // Optional: Log form data for debugging
    for (let [key, value] of formData.entries()) {
        console.log(key + ': ' + value);
    }

    // Make AJAX request to save the package
    $.ajax({
        url: 'http://localhost:8080/api/v1/package/save',
        type: 'POST',
        data: formData,
        contentType: false, // Required for FormData
        processData: false, // Required for FormData
        success: function(response) {
            if (response.code === 201) {
                alert('Package added successfully!');
                $('#addPackageModal').modal('hide');
                $('#addPackageForm')[0].reset();
            } else {
                alert('Failed to add package. Please try again.');
            }
        },
        error: function(error) {
            console.error('Error saving package:', error);
            alert('An error occurred while saving the package.');
        }
    });
});

$(document).ready(function () {
    fetchPackages(); // Call function when the page loads

    function fetchPackages() {
        $.ajax({
            url: `http://localhost:8080/api/v1/package/getAll`,
            type: "GET",
            dataType: "json",
            success: function (response) {
                console.log("API Response:", response.data); // Debug

                let container = $("#packageTableBody");
                container.empty(); // Clear previous data

                response.data.forEach(pkg => {
                    let row = `
                    <tr>
                        <td>${pkg.id}</td>
                        <td>${pkg.name}</td>
                        <td>${pkg.estimatedDays}</td>
                        <td>${pkg.price}</td>
                        <td>
                            ${Array.isArray(pkg.destinations)
                        ? pkg.destinations.join(", ")
                        : pkg.destinations}
                        </td>
                        <td><img src="uploads/${pkg.imageUrl}" width="50" height="50" alt="Package Image"></td>
                        <td>
                            <button class="btn btn-warning btn-sm edit-package-btn" 
                                data-id="${pkg.id}"
                                data-name="${pkg.name}"
                                data-days="${pkg.estimatedDays}"
                                data-price="${pkg.price}"
                                data-img="${pkg.imageUrl}"
                                data-destinations='${JSON.stringify(pkg.destinations)}'
                                data-bs-toggle="modal"
                                data-bs-target="#editPackageModal">
                                Edit
                            </button>
                            <button class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    `;
                    container.append(row);
                });
            },
            error: function (xhr, status, error) {
                console.error("Error fetching packages:", xhr.responseText);
            }
        });
    }
});

$(document).on("click", ".edit-package-btn", function () {
    // Get package details from button attributes
    let packageId = $(this).data("id");
    let packageName = $(this).data("name");
    let packageDays = $(this).data("days");
    let packagePrice = $(this).data("price");
    let packageImage = $(this).data("img");
    let packageDestinations = $(this).data("destinations"); // Should be an array of destination names

    // Fill modal fields
    $("#editPackageId").val(packageId);
    $("#editPackageName").val(packageName);
    $("#editPackageDays").val(packageDays);
    $("#editPackagePrice").val(packagePrice);
    $("#editImageUrl").val(packageImage);
    $("#editPackageDestinations").val(packageDestinations.join(", ")); // Show as comma-separated names

    // Show existing image preview if available
    if (packageImage) {
        $("#editPackageImagePreview").attr("src", "uploads/" + packageImage).show();
    } else {
        $("#editPackageImagePreview").hide();
    }

    // Show modal
    $("#editPackageModal").modal("show");

    // Preview new image when selected
    $("#editPackageImage").on("change", function (event) {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                $("#editPackageImagePreview").attr("src", e.target.result).show();
            };
            reader.readAsDataURL(file);
        }
    });
});



// Image preview for Add Package Modal
document.getElementById('packageImage').addEventListener('change', function (e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('PackageImgPreview').src = e.target.result;
            document.getElementById('PackageImgPreview').style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
});

// Image preview for Edit Package Modal
document.getElementById('editPackageImage').addEventListener('change', function (e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('editPackageImgPreview').src = e.target.result;
            document.getElementById('editPackageImgPreview').style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
});
