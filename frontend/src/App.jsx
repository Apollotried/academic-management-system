import { useState } from 'react';
import './App.css';
import './index.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Landing from './Landing.jsx';
import StudentManagement from './StudentManagement.jsx';
import EditStudent from './components/EditStudent.jsx';
import CoursesManagement from './CoursesManagement.jsx';
import ResultManagement from './ResultManagement.jsx';
import EditCourse from './components/CoursesManagement/EditCourse.jsx';
import AddSyllabus from './components/CoursesManagement/CourseList/addSyllabus.jsx';
import ViewSyllabus from './components/CoursesManagement/CourseList/viewSyllabus.jsx';
import GradeManagement from './GradeManagement.jsx';
import SendNotification from './NotificationEmail.jsx';
import NotificationList from './AfficherEmails.jsx';
import KafkaNotifications from './MessagesKafka.jsx';
import LoginPage from './components/Login/LoginPage.jsx';
import { AuthProvider } from './components/contexe/AuthContext.jsx';
import ToastProvider from './components/ToastProvider.jsx';
import Main from "./components/Main/Main.jsx";

function App() {
    const [count, setCount] = useState(0);

    return (
        <AuthProvider>
            <Router>
                <div>
                    <Routes>

                        <Route path="/" element={<LoginPage />} />


                        <Route path="/admin" element={<ToastProvider />}>
                            <Route path="/admin/landing" element={<Landing />} />
                            <Route path="students" element={<StudentManagement />} />
                            <Route path="edit-student/:id" element={<EditStudent />} />
                            <Route path="edit-course/:id" element={<EditCourse />} />
                            <Route path="add-syllabus" element={<AddSyllabus />} />
                            <Route path="view-syllabus" element={<ViewSyllabus />} />
                            <Route path="courses" element={<CoursesManagement />} />
                            <Route path="results" element={<ResultManagement />} />
                            <Route path="grades" element={<GradeManagement />} />
                            <Route path="send-notification" element={<SendNotification />} />
                            <Route path="notifications" element={<NotificationList />} />
                            <Route path="kafka-notifications" element={<KafkaNotifications />} />
                        </Route>
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
