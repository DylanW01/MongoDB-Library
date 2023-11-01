<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./Assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.2/jquery.min.js" integrity="sha512-tWHlutFnuG0C6nQRlpvrEhE4QpkG1nn2MOUMWmUeRePl4e3Aki0VB6W1v3oLjFtd0hVOtRQ9PHpSfN6u6/QXkQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <!-- Custom styles for bootstrap template-->
    <link href="./Assets/css/sb-admin-2.min.css" rel="stylesheet">
    <title>Library - Loans</title>
    <!-- Get Books -->
    <script>
        function returnBook(loanId, bookId) {
            //alert("Book returned. Refresh page to see updates.");
            const body = {
                loanId: loanId,
                bookId: bookId
            };
            $.post("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/return", body, (data, status) => {
                console.log(data);
                location.reload();
            });
        }
        fetch("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/return").then(
            res => {
                res.json().then(
                    data => {
                        console.log(data);
                        if (data.length > 0) {
                            var temp = "";
                            data.forEach((itemData) => {
                                temp += "<tr>";
                                temp += "<td>" + itemData.id + "</td>";
                                temp += "<td>" + itemData.bookData.Title + "</td>";
                                temp += "<td>" + itemData.bookData.Author + "</td>";
                                temp += "<td>" + itemData.userData.name + "</td>";
                                temp += `<td><button onclick="returnBook('`+itemData.id+`','`+itemData.bookData.id+`')">Return Book</button></td>`;
                            });
                            document.getElementById('data').innerHTML = temp;
                            $('#dataTable').DataTable();
                        }
                    }
                )
            }
        )
    </script>
</head>
<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">
    <jsp:include page="Shared Components/headersidebar.html" />
    <!-- Begin Page Content -->
    <div class="container-fluid">
        <!-- Page Heading -->
        <h1 class="h3 mb-2 text-gray-800">Loans</h1>
        <p class="mb-4">This table lists all active book loans in the library</p>
        <!-- DataTales -->
        <div class="card shadow mb-4">
            <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary">Active Loans</h6>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                        <thead>
                        <tr>
                            <th>Loan ID</th>
                            <th>Book Title</th>
                            <th>Author</th>
                            <th>Customer Name</th>
                            <th>Return Book</th>
                        </tr>
                        </thead>
                        <tfoot>
                        <tr>
                            <th>Loan ID</th>
                            <th>Book Title</th>
                            <th>Author</th>
                            <th>Customer Name</th>
                            <th>Return Book</th>
                        </tr>
                        </tfoot>
                        <tbody id="data">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
    <!-- /.container-fluid -->
</div>
<!-- End of Main Content -->
</div>
<!-- End of Content Wrapper -->
</div>
<!-- End of Page Wrapper -->

<!-- Bootstrap core JavaScript-->
<script src="./Assets/vendor/jquery/jquery.min.js"></script>
<script src="./Assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="./Assets/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="./Assets/js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="./Assets/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="./Assets/vendor/datatables/dataTables.bootstrap4.min.js"></script>
</body>
</html>
