const form = document.getElementById("loginForm");
const errorText = document.getElementById("errorText");

// =====================
// FORM SUBMISSION EVENT
// =====================
form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Clear any previous error messages
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
            console.log(data);

            // SAVE TOKEN
            localStorage.setItem("token", data.token);

            // REDIRECT HOME
            window.location.href = "/";
            return;
        }

        // =====================
        // LOGIN FAILED
        // =====================
        if (errorText) {
            errorText.innerText = data.message || data.error || "Login failed";
        }

    } catch (error) {
        console.error(error);
        if (errorText) {
            errorText.innerText = "Something went wrong";
        }
    }
});