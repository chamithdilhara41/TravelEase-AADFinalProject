document.getElementById("bookNowBtn").addEventListener("click", function (e) {
    e.preventDefault(); // Prevent default <a> behavior

    const token = localStorage.getItem("token");
    const role = getFromLocalStorage("role");

    if (!token) {
        alert("You must be logged in to book.");
        window.location.href = "loginRegister.html"; // redirect to login
        return;
    }

    if (role === "USER" || role === "VEHICLE") {
        window.location.href = "booking.html";
    } else {
        alert("Unauthorized: You don't have permission to book.");
    }
});

function getFromLocalStorage(key) {
    const data = localStorage.getItem(key);
    if (!data) return null;

    const parsedData = JSON.parse(data);
    const now = new Date().getTime();

    if (now > parsedData.expiry) {
        localStorage.removeItem(key);
        return null;
    }
    return parsedData.value;

}