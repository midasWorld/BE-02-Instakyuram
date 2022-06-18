package com.kdt.instakyuram.post.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kdt.instakyuram.member.domain.Member;
import com.kdt.instakyuram.member.dto.MemberResponse;
import com.kdt.instakyuram.member.service.MemberGiver;
import com.kdt.instakyuram.post.domain.Post;
import com.kdt.instakyuram.post.domain.PostImage;
import com.kdt.instakyuram.post.domain.PostRepository;
import com.kdt.instakyuram.post.dto.PostConverter;
import com.kdt.instakyuram.post.dto.PostResponse;

@Transactional(readOnly = true)
@Service
public class PostService {

	private final PostRepository postRepository;
	private final PostConverter postConverter;
	private final MemberGiver memberGiver;
	private final PostImageService postImageService;

	public PostService(PostRepository postRepository, PostConverter postConverter,
		MemberGiver memberGiver, PostImageService postImageService) {
		this.postRepository = postRepository;
		this.postConverter = postConverter;
		this.memberGiver = memberGiver;
		this.postImageService = postImageService;
	}

	@Transactional
	public PostResponse.CreateResponse create(Long memberId, String content, List<MultipartFile> postImages) throws
		IOException {
		// 멤버 조회하기
		Member member = postConverter.toMember(
			memberGiver.findById(memberId)
		);

		//userService.findById() 유저 정보 조회 해오기
		Post savePost = postRepository.save(
			Post.builder()
				.content(content)
				.member(member)
				.build()
		);

		// TODO 이미지 추가로직 처리해야함
		List<PostImage> save = postImageService.save(postImages, savePost.getId());

		return new PostResponse.CreateResponse(savePost.getId(), memberId, content);
	}

	public List<PostResponse.FindAllResponse> findAll(Long memberId) {
		List<MemberResponse> follows = memberGiver.findAllFollowing(memberId);
		List<Member> members = follows.stream()
			.map(postConverter::toMember)
			.toList();

		return postRepository.findByMemberIn(members).stream()
			.map(postConverter::toResponse)
			.toList();
	}

}
