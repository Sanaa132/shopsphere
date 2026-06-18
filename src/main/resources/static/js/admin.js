// ==========================================
// AUTHENTICATION DECODER
// ==========================================
function getRoleFromToken(token) {
    if (!token) return null;
    try {
        const payload = token.split(".")[1];
        const decoded = JSON.parse(atob(payload));
        return decoded.role || null;
    } catch (e) {
        console.error("JWT Parsing Error:", e);
        return null;
    }
}

// ==========================================
// DATA ACQUISITION & CHART RENDERING
// ==========================================
async function loadAnalytics() {
    try {
        // Uses the global 'token' variable defined earlier in admin.html
        const response = await fetch("/api/admin/analytics", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            if (response.status === 403 || response.status === 401) {
                alert("Unauthorized access");
                window.location.href = "/";
            }
            return;
        }

        const data = await response.json();
        console.log("Analytics Data successfully received:", data);

        // ======================================
        // PRODUCT VIEW BAR CHART
        // ======================================
        if (data.productViewData && data.productViewData.length > 0) {
            const productLabels = data.productViewData.map(item => item.productName);
            const productValues = data.productViewData.map(item => item.viewCount);

            new Chart(document.getElementById("viewChart"), {
                type: "bar",
                data: {
                    labels: productLabels,
                    datasets: [{
                        label: "Views",
                        data: productValues,
                        backgroundColor: [
                            "#f97316",
                            "#fbbf24",
                            "#fb923c",
                            "#fdba74",
                            "#ea580c"
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,

                    plugins: {
                        legend: {
                            display: false
                        }
                    },

                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        } else {
            console.warn("No product view data available.");
        }

        // ======================================
        // CATEGORY PIE CHART (WITH EMPTY CHECK)
        // ======================================
        if (data.categoryPurchaseData && data.categoryPurchaseData.length > 0) {
            const categoryLabels = data.categoryPurchaseData.map(item => item.categoryName);
            const categoryValues = data.categoryPurchaseData.map(item => item.totalPurchases);

            new Chart(document.getElementById("categoryChart"), {
                type: "pie",
                data: {
                    labels: categoryLabels,
                    datasets: [{
                        data: categoryValues,
                        backgroundColor: [
                            "#f97316",
                            "#fbbf24",
                            "#fb923c",
                            "#fdba74",
                            "#ea580c"
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,

                    plugins: {
                        legend: {
                            display: true
                        }
                    }
                }
            });
        } else {
            console.warn("No category data found. Pie chart rendering skipped.");
            // Optional: Insert a simple text message inside the container if it's empty
            const chartContainer = document.getElementById("categoryChart").parentElement;
            chartContainer.innerHTML = `<p style="color: #64748b; text-align: center; line-height: 350px;">No sales data recorded yet</p>`;
        }

    } catch (error) {
        console.error("Analytics Error:", error);
    }
}

// ==========================================
// LOGOUT
// ==========================================
function handleLogout(event) {
    event.preventDefault();
    localStorage.removeItem("token");
    window.location.href = "/";
}

// ==========================================
// INITIALIZATION
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    loadAnalytics();

    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", handleLogout);
    }
});