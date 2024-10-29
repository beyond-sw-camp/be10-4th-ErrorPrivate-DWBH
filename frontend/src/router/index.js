import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: "/",
        component: () => import("@/views/main/MainView.vue")
    },
    {
        path: '/chat-list',
        name: 'ChatList',
        component: () => import("@/views/chat/ChatListView.vue")
    },
    {
        path: '/chat/:chatId',
        name: 'ChatRoom',
        component: () => import("@/views/chat/ChatDetailView.vue")
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
