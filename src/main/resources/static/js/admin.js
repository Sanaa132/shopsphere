
const token = localStorage.getItem("token");

// =========================
// JWT ROLE DECODER
// =========================
function getRoleFromToken(token) {

    if (!token) return null;

    try {

        const payload = token.split(".")[1];

        const decoded = JSON.parse(atob(payload));

        return decoded.role || null;

    } catch (e) {

        return null;
    }
}

// =========================
// AUTH CHECK
// =========================
const role = getRoleFromToken(token);

if (!token || role !== "ROLE_ADMIN") {

    window.location.href = "/";
}

// =========================
// LOAD ANALYTICS
// =========================
async function loadAnalytics() {

    const response = await fetch(
        "/api/admin/analytics",
        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    if (!response.ok) {

        alert("Failed to load analytics");

        return;
    }

    const data = await response.json();

    // =========================
    // CATEGORY PIE CHART
    // =========================
    const categoryLabels =
        data.categoryPurchaseData.map(
            item => item.categoryName
        );

    const categoryValues =
        data.categoryPurchaseData.map(
            item => item.totalPurchases
        );

    new Chart(
        document.getElementById("categoryChart"),
        {
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
            }
        }
    );

    // =========================
    // PRODUCT VIEW BAR CHART
    // =========================
    const productLabels =
        data.productViewData.map(
            item => item.productName
        );

    const productValues =
        data.productViewData.map(
            item => item.viewCount
        );

    new Chart(
        document.getElementById("viewChart"),
        {
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
                    ]
                }]
            }
        }
    );
}

loadAnalytics();
