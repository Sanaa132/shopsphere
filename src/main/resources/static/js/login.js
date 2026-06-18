const form = document.getElementById("loginForm");
const errorText = document.getElementById("errorText");

// =====================
// TOAST ALERT DISPATCHER
// =====================
function showToast(message, isError = false) {
    const toast = document.getElementById("toast");
    if (!toast) return;

    // Reset layout classes dynamically
    toast.className = "toast";
    if (isError) {
        toast.classList.add("error");
    }

    toast.textContent = message;
    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 2500);
}

// =====================
// FORM SUBMISSION EVENT
// =====================
form.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (errorText) {
        errorText.innerText = "";
    }

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email,
                password
            })
        });

        const data = await response.json();

        // =====================
        // LOGIN SUCCESS
        // =====================
        if (response.ok) {
            console.log("Login authorized:", data);

            // SAVE STATE VARIABLES
            localStorage.setItem("token", data.token);

            // Display toast feedback layout layer
            showToast("Login successful!");

            // Enforce a deliberate timeout gap to allow the message to be read
            setTimeout(() => {
                window.location.href = "/";
            }, 1000);
            return;
        }

        // =====================
        // LOGIN FAILED
        // =====================
        const failureReason = data.message || data.error || "Login failed";
        if (errorText) {
            errorText.innerText = failureReason;
        }
        showToast(failureReason, true);

    } catch (error) {
        console.error("Authentication submission loop crash:", error);
        if (errorText) {
            errorText.innerText = "Something went wrong";
        }
        showToast("Something went wrong", true);
    }
});