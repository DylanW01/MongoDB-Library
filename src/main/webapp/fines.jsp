<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./Assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for bootstrap template-->
    <link href="./Assets/css/sb-admin-2.min.css" rel="stylesheet">
    <title>Library - Fines</title>
</head>
<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">
    <jsp:include page="Shared Components/headersidebar.html" />
            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Fines</h1>
                <p class="mb-4">This table lists all issued fines for customers</p>

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Fines</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Customer Name</th>
                                    <th>Customer Email</th>
                                    <th>Objects.Book</th>
                                    <th>Amount</th>
                                    <th>Paid</th>
                                </tr>
                                </thead>
                                <tfoot>
                                <tr>
                                    <th>ID</th>
                                    <th>Customer Name</th>
                                    <th>Customer Email</th>
                                    <th>Objects.Book</th>
                                    <th>Amount</th>
                                    <th>Paid</th>
                                </tr>
                                </tfoot>
                                <tbody>
                                <tr>
                                    <td>Tiger Nixon</td>
                                    <td>System Architect</td>
                                    <td>Edinburgh</td>
                                    <td>61</td>
                                </tr>
                                <tr>
                                    <td>Garrett Winters</td>
                                    <td>Accountant</td>
                                    <td>Tokyo</td>
                                    <td>63</td>
                                </tr>
                                <tr>
                                    <td>Ashton Cox</td>
                                    <td>Junior Technical Author</td>
                                    <td>San Francisco</td>
                                    <td>66</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
<jsp:include page="Shared Components/footer.html" />

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

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

<!-- Page level custom scripts -->
<script src="./Assets/js/demo/datatables-demo.js"></script>
</body>
</html>
