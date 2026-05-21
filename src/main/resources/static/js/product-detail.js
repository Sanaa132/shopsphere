const token = localStorage.getItem("token");

// ================= TOAST =================
function showToast(message, type = "success") {
    const toast = document.getElementById("toast");

    toast.textContent = message;

    toast.style.background =
        type === "error" ? "#ef4444" : "#f97316";

    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

// ================= GET PRODUCT ID FROM URL =================
const productId = window.location.pathname.split("/").pop();

// ================= LOAD PRODUCT =================
async function loadProduct() {

    const res = await fetch(`/api/products/${productId}`);

    if (!res.ok) {
        showToast("Failed to load product", "error");
        return;
    }

    const p = await res.json();

    // IMAGE
    document.getElementById("productImage").src =
        p.imageUrl || "/images/placeholder.jpg";

    // NAME
    document.getElementById("productName").textContent = p.name;

    // CATEGORY
    document.getElementById("categoryName").textContent =
        p.categoryName || "";

    // PRICE
    document.getElementById("discountPrice").textContent =
        `₹${p.discountedPrice || p.price}`;

    if (p.discountedPrice && p.discountedPrice < p.price) {
        document.getElementById("originalPrice").textContent =
            `₹${p.price}`;
    } else {
        document.getElementById("originalPrice").style.display = "none";
    }

    // DESCRIPTION
    document.getElementById("description").textContent =
        p.description || "";

    // STOCK RULE
    const stockEl = document.getElementById("stockInfo");

    if (p.stockQuantity < 50) {
        stockEl.textContent = `In Stock: ${p.stockQuantity} items`;
        stockEl.style.color = p.stockQuantity > 0 ? "#16a34a" : "#ef4444";
    } else {
        stockEl.style.display = "none";
    }

    // BUTTONS
    document.getElementById("addToCartBtn").onclick =
        () => addToCart(p.id);

    document.getElementById("addToWishlistBtn").onclick =
        () => addToWishlist(p.id);
}

// ================= ADD TO CART =================
async function addToCart(id) {

    if (!token) {
        showToast("Please login to add to cart", "error");
        return;
    }

    const res = await fetch(`/api/cart/add?productId=${id}&quantity=1`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (res.ok) {
        showToast("Added to cart!", "success");
    } else {
        const data = await res.json();
        showToast(data.message || "Failed to add", "error");
    }
}

// ================= ADD TO WISHLIST =================
async function addToWishlist(id) {

    if (!token) {
        showToast("Please login to add to wishlist", "error");
        return;
    }

    const res = await fetch(`/api/wishlist/add?productId=${id}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (res.ok) {
        showToast("Added to wishlist!", "success");
    } else {
        const data = await res.json();
        showToast(data.message || "Failed to add", "error");
    }
}

// INIT
loadProduct();