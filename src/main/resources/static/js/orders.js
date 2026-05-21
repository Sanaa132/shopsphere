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

async function loadOrders() {

    const response = await fetch(

        "/api/orders/history",

        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    const container =
        document.getElementById("ordersContainer");

    if (!response.ok) {

        showToast("Failed to load orders");

        return;
    }

    const orders = await response.json();

    container.innerHTML = "";

    if (orders.length === 0) {

        container.innerHTML = `
            <p>No orders yet</p>
        `;

        return;
    }

    orders.forEach(order => {

        let itemsHtml = "";

        order.items.forEach(item => {

            itemsHtml += `

                <div class="order-content">

                    <img
                        src="https://via.placeholder.com/130"
                        class="order-image"
                        alt="product"
                    >

                    <div class="order-details">

                        <h3>${item.productName}</h3>

                        <p>
                            Quantity: ${item.quantity}
                        </p>

                        <p>
                            Price: ₹${item.price}
                        </p>

                    </div>

                </div>
            `;
        });

        container.innerHTML += `

            <div class="order-card">

                <div class="order-top">

                    <div class="order-id">
                        Order #${order.orderId}
                    </div>

                    <div class="order-status">
                        ${order.status}
                    </div>

                </div>

                ${itemsHtml}

                <div class="order-bottom">

                    <div class="order-total">
                        Total: ₹${order.totalAmount}
                    </div>

                </div>

            </div>
        `;
    });
}

loadOrders();