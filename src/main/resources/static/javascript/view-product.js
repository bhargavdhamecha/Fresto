document.addEventListener('DOMContentLoaded', function () {
console.log("DOM is ready!");
    const input = document.getElementById('quantity-input');
    const btnMinus = document.querySelector('.btn-minus');
    const btnPlus = document.querySelector('.btn-plus');

    input.value = 1;
    btnMinus.addEventListener('click', () => {
      let value = parseInt(input.value) || 1;
      if (value > parseInt(input.min)) {
        input.value = value - 1;
      }
    });

    btnPlus.addEventListener('click', () => {
      let value = parseInt(input.value) || 1;
      input.value = value + 1;
    });
  });