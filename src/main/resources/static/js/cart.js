const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "/login-page"; // Redirects to your login route if token is missing
}

function showToast(message) {
    const toast = document.getElementById("toast");
    if (toast) {
        toast.textContent = message;
        toast.classList.add("show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 2500);
    }
}

function showErrorToast(message) {
    const toast = document.getElementById("toast");
    if (toast) {
        toast.textContent = message;
        toast.style.background = "#dc2626";
        toast.classList.add("show");
        setTimeout(() => {
            toast.classList.remove("show");
            toast.style.background = "#0f172a";
        }, 3000);
    }
}

// CHECK LOGIN
if (!token) {
    window.location.href = "/login-page";
}

let cartItems = [];

// LOAD CART
async function loadCart() {
    const response = await fetch("/api/cart", {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        showErrorToast("Failed to load cart");
        return;
    }

    const cart = await response.json();
    cartItems = cart.items;

    const container = document.getElementById("cartContainer");
    if (!container) return;

    container.innerHTML = "";

    // EMPTY CART
    if (cart.items.length === 0) {
        container.innerHTML = `
            <p style="margin-top:20px;">
                Your cart is empty
            </p>
        `;
        const totalDiv = document.getElementById("cartTotal");
        if (totalDiv) totalDiv.innerHTML = "";
        return;
    }

    // LOAD CART ITEMS
    cart.items.forEach(item => {
        const subtotal = item.price * item.quantity;

        container.innerHTML += `
            <div class="cart-item" id="cart-item-${item.cartItemId}">
                <img
                    src="${item.imageUrl || '/images/placeholder.jpg'}"
                    alt="${item.productName || 'product'}"
                    class="cart-image"
                >

                <div class="cart-details">
                    <h3>${item.productName}</h3>

                    <p class="cart-price">
                        ₹${item.price}
                    </p>

                    <p class="item-subtotal">
                        Subtotal: ₹${subtotal}
                    </p>

                    <div class="quantity-controls">
                        <button
                            class="quantity-btn"
                            onclick="decreaseQuantity(${item.cartItemId})"
                        >
                            -
                        </button>

                        <span class="quantity-number item-quantity">
                            ${item.quantity}
                        </span>

                        <button
                            class="quantity-btn"
                            onclick="increaseQuantity(${item.cartItemId})"
                        >
                            +
                        </button>

                        <button
                            class="remove-btn"
                            onclick="removeItem(${item.cartItemId})"
                        >
                            Remove
                        </button>
                    </div>
                </div>
            </div>
        `;
    });

    // TOTAL PRICE
    const totalDiv = document.getElementById("cartTotal");
    if (totalDiv) {
        totalDiv.innerHTML = `<h3>Total: ₹${cart.totalPrice}</h3>`;
    }
}

// INCREASE QUANTITY
async function increaseQuantity(cartItemId) {
    await updateQuantity(cartItemId, 1);
}

// DECREASE QUANTITY
async function decreaseQuantity(cartItemId) {
    await updateQuantity(cartItemId, -1);
}

// STEP 4: REPLACED ENTIRE updateQuantity FUNCTION WITH SMOOTH DOM UPDATES
async function updateQuantity(cartItemId, change) {

    const item = cartItems.find(i => i.cartItemId === cartItemId);

    if (!item) return;

    const newQuantity = item.quantity + change;

    // REMOVE ITEM IF QUANTITY <= 0
    if (newQuantity <= 0) {
        await removeItem(cartItemId);
        return;
    }

    const response = await fetch(
        `/api/cart/update/${cartItemId}?quantity=${newQuantity}`,
        {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    if (response.ok) {

        // UPDATE LOCAL MEMORY
        item.quantity = newQuantity;

        // FIND CURRENT CARD
        const card = document.getElementById(`cart-item-${cartItemId}`);

        if (!card) return;

        // UPDATE QUANTITY
        card.querySelector(".item-quantity").textContent =
            newQuantity;

        // UPDATE SUBTOTAL
        const subtotal = item.price * newQuantity;

        card.querySelector(".item-subtotal").textContent =
            `Subtotal: ₹${subtotal}`;

        // UPDATE TOTAL
        const total = cartItems.reduce(
            (sum, i) => sum + (i.price * i.quantity),
            0
        );

        const totalDiv = document.getElementById("cartTotal");

        if (totalDiv) {
            totalDiv.innerHTML =
                `<h3>Total: ₹${total.toFixed(2)}</h3>`;
        }

    } else {

        const data = await response.json();

        showErrorToast(
            data.message ||
            data.error ||
            "Failed to update quantity"
        );
    }
}

// REMOVE ITEM
async function removeItem(cartItemId) {
    const response = await fetch(`/api/cart/remove/${cartItemId}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (response.ok) {
        loadCart();
    } else {
        const data = await response.json();
        showErrorToast(data.message || data.error || "Failed to remove item");
    }
}

// PLACE ORDER
const placeOrderBtn = document.getElementById("placeOrderBtn");
if (placeOrderBtn) {
    placeOrderBtn.addEventListener("click", async () => {
        if (cartItems.length === 0) {
            showToast("Cart is empty");
            return;
        }

        const cartItemIds = cartItems.map(item => item.cartItemId);

        const response = await fetch("/api/orders/place", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({
                cartItemIds: cartItemIds
            })
        });

        if (response.ok) {
            showToast("Order placed successfully!");
            setTimeout(() => {
                window.location.href = "/orders-page";
            }, 1500);
        } else {
            const data = await response.json();
            showErrorToast(data.message || data.error || "Order failed");
        }
    });
}

// INITIAL LOAD
loadCart();