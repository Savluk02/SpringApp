function showFields() {
    const addButton = document.getElementById('addButton');
    const orderFields = document.getElementById('orderFields');

    if (orderFields.style.display === 'none') {
        orderFields.style.display = 'block';
        addButton.innerText = 'Collapse';
    } else {
        orderFields.style.display = 'none';
        addButton.innerText = 'Add';
    }
}