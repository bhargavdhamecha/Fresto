 console.log("Toast script loaded");

  document.addEventListener('DOMContentLoaded', function () {
           console.log("DOM fully loaded and parsed");
          const toastEl = document.querySelector('.toast');
          if (toastEl) {
              const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
              toast.show();
          }
      });
