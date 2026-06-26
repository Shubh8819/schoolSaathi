// Sidebar Toggle
document.getElementById('sidebarCollapse')
    ?.addEventListener('click', function () {
        document.getElementById('sidebar')
            .classList.toggle('active');
    });

// Auto hide alerts after 4 seconds
document.querySelectorAll('.alert').forEach(alert => {
    setTimeout(() => {
        alert.style.transition = 'opacity 0.5s';
        alert.style.opacity = '0';
        setTimeout(() => alert.remove(), 500);
    }, 4000);
});