const token = localStorage.getItem("token");

if (!token) {

    window.location.href = "/login-page";
}

function showToast(message) {

    const toast = document.getElementById("toast");

    toast.textContent = message;

    toast.classList.add("show");

    setTimeout(() => {

        toast.classList.remove("show");

    }, 2500);
}

function showErrorToast(message) {

    const toast = document.getElementById("toast");

    toast.textContent = message;

    toast.style.background = "#dc2626";

    toast.classList.add("show");

    setTimeout(() => {

        toast.classList.remove("show");

        toast.style.background = "#0f172a";

    }, 2500);
}

// LOAD WISHLIST
async function loadWishlist() {

    const response = await fetch(

        "/api/wishlist",

        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    const grid = document.getElementById("wishlistGrid");

    if (!response.ok) {

        showErrorToast("Failed to load wishlist");

        return;
    }

    const products = await response.json();

    grid.innerHTML = "";

    // EMPTY WISHLIST
    if (products.length === 0) {

        grid.innerHTML = `
            <p>Your wishlist is empty</p>
        `;

        return;
    }

    // LOAD PRODUCTS
    products.forEach(product => {

        grid.innerHTML += `

            <div class="product-card">

                <h3>${product.name}</h3>

                <p>${product.description || ""}</p>

                <p class="price">
                    ₹${product.discountedPrice || product.price}
                </p>

                <p style="
                    text-decoration: line-through;
                    color: #94a3b8;
                ">
                    ₹${product.price}
                </p>

                <button
                    onclick="addToCart(${product.id}, '${product.name}')"
                >
                    Add to Cart
                </button>

                <button
                    onclick="removeFromWishlist(${product.id})"
                    style="
                        background:#dc2626;
                        margin-top:8px;
                    "
                >
                    Remove
                </button>

            </div>
        `;
    });
}

// ADD TO CART
async function addToCart(productId, productName) {

    const response = await fetch(

        `/api/cart/add?productId=${productId}&quantity=1`,

        {
            method: "POST",

            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    if (response.ok) {

        showToast(`${productName} added to cart`);
    }

    else {

        const data = await response.json();

        showErrorToast(
            data.error || "Failed to add to cart"
        );
    }
}

// REMOVE FROM WISHLIST
async function removeFromWishlist(productId) {

    const response = await fetch(

        `/api/wishlist/remove?productId=${productId}`,

        {
            method: "DELETE",

            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    if (response.ok) {

        showToast("Removed from wishlist");

        loadWishlist();
    }

    else {

        const data = await response.json();

        showErrorToast(
            data.error || "Failed to remove item"
        );
    }
}

// INITIAL LOAD
loadWishlist();