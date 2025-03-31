const container = document.querySelector('.container');
const registerBtn = document.querySelector('.register-btn');
const loginBtn = document.querySelector('.login-btn');

registerBtn.addEventListener('click', () => {
    container.classList.add('active');
})

loginBtn.addEventListener('click', () => {
    container.classList.remove('active');
})

const API_BASE = "http://localhost:8080/api/v1";
document.addEventListener("DOMContentLoaded", function () {
    const phoneInputField = document.querySelector("#contact");
    const phoneError = document.querySelector("#phone-error");
    const signupBtn = document.querySelector("#signupBtn");

    // Initialize intl-tel-input
    const iti = window.intlTelInput(phoneInputField, {
        initialCountry: "lk", // Default country (Sri Lanka)
        preferredCountries: ["lk", "us", "gb", "in", "au"],
        separateDialCode: true,
        utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/utils.js",
    });

    // Function to validate phone number in real-time
    function validatePhoneNumber() {
        const phoneNumber = phoneInputField.value.trim();
        // Regex for Sri Lanka phone numbers starting with 07 and followed by 8 digits
        const phoneRegex = /^07\d{8}$/;

        // Check if the number is valid with intl-tel-input and matches the regex
        if (phoneNumber !== "" && (!iti.isValidNumber() || !phoneRegex.test(phoneNumber))) {
            phoneError.style.display = "block";
            phoneError.textContent = "Invalid phone number format!";
            phoneInputField.classList.add("invalid");
            signupBtn.disabled = true; // Disable registration if invalid
        } else {
            phoneError.style.display = "none";
            phoneInputField.classList.remove("invalid");
            signupBtn.disabled = false; // Enable registration
        }
    }

    // Validate while typing
    phoneInputField.addEventListener("input", validatePhoneNumber);

    // Prevent form submission if phone number is invalid
    document.querySelector("form").addEventListener("submit", function (event) {
        const phoneNumber = phoneInputField.value.trim();
        if (!iti.isValidNumber() || !/^07\d{8}$/.test(phoneNumber)) {
            event.preventDefault();
            phoneError.style.display = "block";
            phoneError.textContent = "Please enter a valid phone number!";
        }
    });

    // Updated Signup Function
    $("#signupBtn").click(function () {
        let name = $("#name").val();
        let email = $("#signupEmail").val();
        let contact = iti.getNumber(); // Get full international format
        let password = $("#signupPassword").val();

        // Disable button to prevent multiple clicks
        $("#signupBtn").prop("disabled", true).text("Registering...");

        $.ajax({
            url: `${API_BASE}/user/register`,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ name, email, contact, password }),
            success: function (response) {
                alert("✅ Signup Successful!");
                window.location.reload();
            },
            error: function (xhr) {
                let errorMessage = xhr.responseJSON?.message || "Unknown error";
                alert("❌ Signup Failed: " + errorMessage);
            },
            complete: function () {
                $("#signupBtn").prop("disabled", false).text("Register");
            }
        });
    });
});

/**************** Sign in ****************/

// Login function
$("#loginBtn").click(function () {
    let email = $("#loginEmail").val();
    let password = $("#loginPassword").val();

    $.ajax({
        url: `${API_BASE}/auth/authenticate`,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ email, password }),
        success: function (response) {
            localStorage.setItem("token", response.data.token);
            checkUserRole();
        },
        error: function (xhr) {
            alert("Login Failed: " + xhr.responseJSON.message);
        }
    });
});

function checkUserRole() {
    let token = localStorage.getItem("token");
    if (!token) {
        showDashboard("Unauthorized: Please log in.");
        return;
    }

    // Function to check role using a given endpoint
    function checkRole(url, successCallback, failureCallback) {
        $.ajax({
            url: `${API_BASE}/admin/${url}`,  // Correct template literal usage
            type: "GET",
            headers: { "Authorization": `Bearer ${token}` },  // Correct template literal usage
            success: successCallback,
            error: function(jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 403) {
                    // Hide 403 error from console
                    console.log(`Access Denied for ${url}, no need to log error.`);
                    if (failureCallback) {
                        failureCallback(jqXHR, textStatus, errorThrown);  // Pass to failureCallback for further action
                    }
                } else {
                    // Log other errors normally
                    console.error(`Error checking role at ${url}:`, textStatus, errorThrown);
                    if (failureCallback) {
                        failureCallback(jqXHR, textStatus, errorThrown);  // Pass to failureCallback for further action
                    }
                }
            }
        });
    }

    // First, check if the user is an Admin (test1)
    checkRole("test1",
        function () {
            console.log("hi admin");  // If success, user is Admin
        },
        function (jqXHR, textStatus, errorThrown) {
            console.log("Admin check failed, checking if user is valid...");

            // If not an Admin, check if they are a User (test2)
            checkRole("test2",
                function () {
                    console.log("hi user");  // If success, user is User
                },
                function (jqXHR, textStatus, errorThrown) {
                    console.log("User check failed, checking if vehicle role exists...");

                    // If test2 fails, check test3 as another User validation
                    checkRole("test3",
                        function () {
                            console.log("hi vehicle");  // If success, user is Vehicle User
                        },
                        function () {
                            showDashboard("Unauthorized: Access denied.");  // If all role checks fail
                        }
                    );
                }
            );
        }
    );
}

