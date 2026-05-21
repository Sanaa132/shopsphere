const token = localStorage.getItem("token");

// =====================
// JWT ROLE DECODER
// =====================
function getRoleFromToken(token) {
    if (!token) return null;
    try {
        const payload = token.split(".")[1];
        return JSON.parse(atob(payload)).role || null;
    } catch (e) {
        return null;
    }
}

// =====================
// TOAST SYSTEM
// =====================
function showToast(message, type = "success") {
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
// INITIAL SETUP
// =====================
document.addEventListener("DOMContentLoaded", () => {
    // =====================
    // LOGIN / LOGOUT LOGIC
    // =====================
    const authLink = document.getElementById("authLink");

    if (token && authLink) {
        authLink.textContent = "Logout";
        authLink.href = "#";
        authLink.onclick = () => {
            localStorage.removeItem("token");
            window.location.reload();
        };
    }

    // =====================
    // LOAD PRODUCTS
    // =====================
    loadProducts();
});

// =====================
// LOAD PRODUCTS
// =====================
async function loadProducts() {
    try {
        const response = await fetch("/api/products");

        if (!response.ok) {
            showToast("Failed to load products", "error");
            return;
        }

        const products = await response.json();
        const grid = document.getElementById("productGrid");

        // IMPORTANT SAFETY CHECK
        if (!grid) {
            console.error("productGrid element not found");
            return;
        }

        grid.innerHTML = "";

        products.forEach(product => {
            const card = document.createElement("div");
            card.className = "product-card";

            // =====================
            // PRICE LOGIC
            // =====================
            const finalPrice = product.discountedPrice && product.discountedPrice < product.price
                ? product.discountedPrice
                : product.price;

            const originalPriceHTML = product.discountedPrice && product.discountedPrice < product.price
                ? `<p style="text-decoration:line-through; color:#94a3b8;">₹${product.price}</p>`
                : "";

            // =====================
            // STOCK LOGIC
            // =====================
            const stockText = product.stockQuantity > 0
                ? `<p style="color:green; font-weight:500;">In Stock (${product.stockQuantity})</p>`
                : `<p style="color:red; font-weight:500;">Out of Stock</p>`;

            const buttonHTML = product.stockQuantity > 0
                ? `<button onclick="addToCart(${product.id})">Add to Cart</button>`
                : `<button disabled style="background:gray; cursor:not-allowed;">Out of Stock</button>`;

            card.innerHTML = `
                <img src="${product.imageUrl || '/images/placeholder.jpg'}"
                     style="width:100%; height:220px; object-fit:cover; border-radius:10px; margin-bottom:15px;"
                     alt="${product.name}">
                <h3>${product.name}</h3>
                <p>${product.description || ""}</p>
                <p class="price" style="font-weight:bold; font-size:1.1rem; margin-top:5px;">₹${finalPrice}</p>
                ${originalPriceHTML}
                ${stockText}
                <div style="display:flex; gap:10px; margin-top:15px;">
                    ${buttonHTML}
                    <button onclick="addToWishlist(${product.id})" class="btn-secondary">Wishlist</button>
                </div>
            `;

            grid.appendChild(card);
        });

    } catch (error) {
        console.error(error);
        showToast("Something went wrong", "error");
    }
}

// =====================
// ADD TO CART
// =====================
async function addToCart(productId) {
    try {
        if (!token) {
            window.location.href = "/login-page";
            return;
        }

        const response = await fetch(`/api/cart/add?productId=${productId}&quantity=1`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            showToast("Added to cart!", "success");
        } else {
            showToast("Failed to add to cart", "error");
        }

    } catch (error) {
        console.error(error);
        showToast("Something went wrong", "error");
    }
}

// =====================
// ADD TO WISHLIST
// =====================
async function addToWishlist(productId) {
    try {
        if (!token) {
            window.location.href = "/login-page";
            return;
        }

        const response = await fetch(`/api/wishlist/add?productId=${productId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            showToast("Added to wishlist!", "success");
        } else {
            showToast("Failed to add to wishlist", "error");
        }

    } catch (error) {
        console.error(error);
        showToast("Something went wrong", "error");
    }
}