let duplicatePhoneConfirmed = false;

const form = document.getElementById("registerForm");

// =====================
// GLOBAL TOAST SYSTEM
// =====================
function showToast(message, type = "error") {
    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    toast.innerText = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.classList.add("show");
    }, 50);

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 3000);
}

// =====================
// REGISTER SUBMISSION
// =====================
form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const phoneNumber = document.getElementById("phoneNumber").value;

    try {
        const response = await fetch("/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name,
                email,
                password,
                phoneNumber
            })
        });

        const data = await response.json();

        // =====================
        // EMAIL CONFLICT (409)
        // =====================
        if (response.status === 409) {
            showToast("Email already exists", "error");
            return;
        }

        // ==========================================
        // PHONE NUMBER WARNING (RUNS AFTER EMAIL CHECK)
        // ==========================================
        const existingUsers = JSON.parse(localStorage.getItem("usedPhones")) || [];

        if (existingUsers.includes(phoneNumber) && !duplicatePhoneConfirmed) {
            duplicatePhoneConfirmed = true;

            showToast(
                "Phone number already exists. Click register again to continue.",
                "info"
            );
            return;
        }

        // =====================
        // SUCCESS
        // =====================
        if (response.ok) {
            localStorage.setItem("token", data.token);

            // Save phone number locally to trigger the warnings for future registrations
            const usedPhones = JSON.parse(localStorage.getItem("usedPhones")) || [];
            usedPhones.push(phoneNumber);
            localStorage.setItem("usedPhones", JSON.stringify(usedPhones));

            showToast("Registration successful!", "success");

            // Reset confirmation status for clean subsequent states
            duplicatePhoneConfirmed = false;

            // Brief delay to let the success toast show before redirecting
            setTimeout(() => {
                window.location.href = "/";
            }, 1000);
            return;
        }

        // ==========================================
        // DTO VALIDATION ERRORS (Reads Flat Error Map)
        // ==========================================
        const firstError = Object.values(data)[0];

        showToast(
            firstError || "Registration failed",
            "error"
        );

    } catch (error) {
        console.error(error);
        showToast("Something went wrong", "error");
    }
});